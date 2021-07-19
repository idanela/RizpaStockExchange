var TransactionsVersion = 0;
var notificationVersion= 0;
var amountFreeToUse =0;
function addStock(stockToAdd) {
    $("#symbol").empty();
    $("#company").empty();
    $("#currentPrice").empty();
    $("#sumOfTransactions").empty();
    $("#numOfStocks").empty();

    $("#symbol").append("Symbol: " + stockToAdd.stock.stockName);
    $("#company").append("Company: " + stockToAdd.stock.stockHolder);
    $("#currentPrice").append("Price: " + stockToAdd.stock.currentPrice);
    $("#sumOfTransactions").append("Sum of transactions: "+stockToAdd.stock.transactionsWorth);
    if(stockToAdd.amount >= 0)
    {
        $("#numOfStocks").append("amount: "+ stockToAdd.amount)
    }
}

function getStockDetails() {
    $.ajax({
        data: "symbol=" + sessionStorage.getItem("symbol"),
        url: "/loadStock",
        method:"get",
        timeout: 2000,
        error: function (errorObject) {
            console.log("Failed to load Stock");
        },
        success: function (stock) {
            var stockToAdd = JSON.parse(stock);
            addStock(stockToAdd);
            amountFreeToUse = stockToAdd.amountFreeToUse;
        }
    });
}

function addToTransactionsTable(newResp) {
    $.each(newResp.transactions || [], function(index, transaction) {
       var toInsert =  $('<tr><td>' + transaction.date + '</td>' +
            '<td>'+ transaction.amount +'</td>' +
            '<td>' + transaction.price+ '</td>' +
            '</tr>');
       $("#stockTransactionBody").prepend(toInsert);
/*
        $('#stockTransactionsTable tr').eq(1).after(toInsert);
*/
    })
}

function fillStockTransactions() {
    var name = $("#symbol")[0].innerText.split(' ')[1];
    var params = "symbol="+ name +"&version="+TransactionsVersion
    $.ajax({
            data:params,
            url:"/transactions",
            method:"get",
            timeout:2000,
            error: function(errorObject) {
                console.log("Failed to get transactions");
                triggerFillTransactionsTable();
            },
            success: function(resp) {
                var newResp = JSON.parse(resp);
                if(newResp.version!==TransactionsVersion)
                {
                    TransactionsVersion = newResp.version;
                    addToTransactionsTable(newResp);
                }
                triggerFillTransactionsTable();
            }
        })
}

function triggerFillTransactionsTable() {
    setTimeout(fillStockTransactions ,2000);

}

function isNormalInteger(val) {
        var n = Math.floor(Number(val));
        return n !== Infinity && String(n) === val && n > 0;
    }

function addToNotificationsTable(newResp) {
    $.each(newResp.transactions || [], function(index, transaction) {
       var toInsert = $('<tr><td>' +transaction.instruction+'</td>>'+
            '<td>' +transaction.kind+'</td>>'+
            '<td>' + transaction.symbol + '</td>' +
            '<td>'+ transaction.amount +'</td>' +
            '<td>' + transaction.price+ '</td>' +
            '</tr>');
       $("#notBody").prepend(toInsert);
        /*$('#transactionTable tr').eq(1).after(toInsert);*/
    })
}

function getNotifications() {
    $.ajax({
        data:"version="+notificationVersion,
        url:"/notifications",
        timeout:2000,
        success(resp)
        {
            var newResp = JSON.parse(resp);
            if(newResp.version!==notificationVersion)
            {
                notificationVersion = newResp.version;
                addToNotificationsTable(newResp);
                getStockDetails();
            }
            triggerGetNotifications();
        },
        error(err)
        {
            triggerGetNotifications();

        }
    })
}

function triggerGetNotifications() {
   setTimeout(getNotifications,2000);
}

function setAndStartPullNotifications() {

    $.ajax({
        url:"/setNotificationsVersion",
        timeout:2000,
        success(resp)
        {
            if(resp!==notificationVersion) {
                notificationVersion = Math.floor(Number(resp));
            }
            triggerGetNotifications();
        },
        error(err){
            triggerGetNotifications();
        }
    })
}

function setNotificationVersion() {
    setTimeout(setAndStartPullNotifications ,2000);

}

function setPopUp(resp) {
    var transactionDetails = JSON.parse(resp);
    var numExecuted = transactionDetails.numOfExecuted;
    if(transactionDetails.isBought)
    {
        $(".modal-body p")[0].innerText= numExecuted+" stocks has been bought";
        if(transactionDetails.residual !==0)
        {
            if(!$("#fok").is(':checked') && !$("#ioc").is(':checked'))
                $(".modal-body p")[1].innerText = transactionDetails.residual+" stocks has been added to pending buy transactions";
            else
                $(".modal-body p")[1].innerText ='';
        }
        else
            $(".modal-body p")[1].innerText = "Transaction made fully";
    }
    else
    {
        if(!$("#fok").is(':checked') && !$("#ioc").is(':checked'))
        {
            amountFreeToUse = amountFreeToUse - (numExecuted+transactionDetails.residual);
        }
        else
        {
            amountFreeToUse = amountFreeToUse - numExecuted;
        }
        $(".modal-body p")[0].innerText= numExecuted+" stocks has been sold";
        if(transactionDetails.residual !==0) {
            if(!$("#fok").is(':checked') && !$("#ioc").is(':checked'))
                $(".modal-body p")[1].innerText = transactionDetails.residual+" stocks has been added to pending sell transactions";
            else
                $(".modal-body p")[1].innerText ='';
        }
        else
        $(".modal-body p")[1].innerText="Transaction made fully";

    }
    $("#myBtn").trigger('click');

}

$(function () {
    $("#userNameDiv").text("Hello "+sessionStorage.getItem("username"));

    $("#amountOfStocks").keyup(function ()
    {
        var amount = $("#amountOfStocks").val();
        $("#amountError").empty();

        if(!isNormalInteger(amount)) {
            if(amount!=='')
            $("#amountError").append("Amount should be a positive number!!");
        }
        else{
            if($("#sell").is(':checked'))
            {
                if(amount > amountFreeToUse)
                $("#amountError").append("you can sell up to "+amountFreeToUse+" stocks");
            }
        }
        if(amount ==='' || !isNormalInteger(amount) ||$("#sell").is(':checked') &&amount > amountFreeToUse )
            $(':input[type="submit"]').prop('disabled', true);
        else
        {
            if($("#limit").val()!=='')
            {
                 if(isNormalInteger($("#limit").val()))
                 {
                     $(':input[type="submit"]').prop('disabled', false);
                 }
            }
        }

    });

    $("#limit").keyup(function ()
    {
        var limit = $("#limit").val();
        $("#limitError").empty();

        if(!isNormalInteger(limit)) {
            if(limit!=='')
            $("#limitError").append("Limit should be a positive number!!");
        }

        if(limit ==='' || !isNormalInteger(limit) ||$("#sell").is(':checked') &&$("#amountOfStocks").val() >amountFreeToUse)
            $(':input[type="submit"]').prop('disabled', true);
        else
        {
            if($("#amountOfStocks").val()!=='')
            {
                if(isNormalInteger($("#amountOfStocks").val()))
                {
                    $(':input[type="submit"]').prop('disabled', false);
                }
            }
        }

    });


    getStockDetails();
    $("#preformTransaction").submit(function () {
        var name = $("#symbol")[0].innerText.split(' ')[1];
        var symbol="&symbol="+name;
        var data = $(this).serialize() +symbol;
        $.ajax({
            data: data,
            url: this.action,
            method:'post',
            timeout: 2000,
            error: function (errorObject) {
                console.error("Failed to load Stock");
            },
            success: function (resp) {
                if(!$('#mkt').prop('checked'))
                {
                    $("#limit").val('');
                }
                $("#amountOfStocks").val('');
                $(':input[type="submit"]').prop('disabled', true);
                setPopUp(resp)
            }
        });
        getStockDetails();
        return false;
    });
    triggerFillTransactionsTable();
    setNotificationVersion();

    $('.radioInstruction').on('click', function(){
        $('#limitError').empty();
        if($('#mkt').prop('checked')){
            $('#limit').val('1');
            $("#amountOfStocks").trigger('keyup');
            if($("#amountOfStocks").val()==='')
            {
                $("#amountError").empty();
            }
            if($("#amount"))
            $('#limitLabel').hide();
            $('#limit').hide();
        } else {
            $('#limitLabel').show();
            $(':input[type="submit"]').prop('disabled', true);
            $('#limit').val('');
            $('#limit').show();
        }
    });


    var modal = document.getElementById("myModal");

// Get the button that opens the modal
    var btn = document.getElementById("myBtn");

// Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];

// When the user clicks the button, open the modal
    btn.onclick = function() {
        modal.style.display = "block";
    }

// When the user clicks on <span> (x), close the modal
    span.onclick = function() {
        modal.style.display = "none";
    }

// When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }

    $('.typeOfAction').on('click', function(){
        $("#amountOfStocks").trigger('keyup');
    });
});
