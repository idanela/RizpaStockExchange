var version = 0;
var notificationVersion = 0;
function isNormalInteger(val) {
    var n = Math.floor(Number(val));
    return n !== Infinity && String(n) === val && n > 0;
}

function checkAmount() {
    if(isNormalInteger($("#amountOfStocks").val()))
        return true
    else
    {
        return false
    }
}

function checkWorth() {
    if(isNormalInteger($("#companyWorth").val()))
        return true
    else
    {
        return false
    }
}

function setValidationCheckForCreateStock() {
    $("#stockCompanyName").keyup(function ()
    {
        if($("#stockCompanyName").val() !=='' && $("#stockSymbol").val()!=='' && checkAmount() &&checkWorth())
        {
            $('#createStockButton').prop('disabled', false);
        }
        else
        {
            $('#createStockButton').prop('disabled', true);

        }
    });

    $("#stockSymbol").keyup(function ()
    {
        if($("#stockCompanyName").val() !=='' && $("#stockSymbol").val()!=='' && checkAmount() &&checkWorth())
        {
            $('#createStockButton').prop('disabled', false);
        }
        else
        {
            $('#createStockButton').prop('disabled', true);

        }
    }) ;
    $("#amountOfStocks").keyup(function ()
    {
        $("#amountErr").empty();
        if($("#stockCompanyName").val() !=='' && $("#stockSymbol").val()!=='' && checkAmount() &&checkWorth())
        {
            $('#createStockButton').prop('disabled', false);
        }
        else
        {
            if(!checkAmount() && $('#amountOfStocks').val()!=='')
            $("#amountErr").append("Amount should be a positive number")
            $('#createStockButton').prop('disabled', true);
        }
    });
    $("#companyWorth").keyup(function ()
    {
        $("#worthErr").empty();
        if($("#stockCompanyName").val() !=='' && $("#stockSymbol").val()!=='' && checkAmount() &&checkWorth())
        {
            $('#createStockButton').prop('disabled', false);
        }
        else
        {
            if(!checkWorth() && $('#companyWorth').val()!=='')
            $("#worthErr").append("Company's worth should be a positive number")
            $('#createStockButton').prop('disabled', true);
        }
    })

}

function addToNotificationsModal(newResp) {
    $.each(newResp.notifications || [], function(index, notification) {
        var message = document.createElement("p");

        if (notification.isBought === false) {
            message.innerText = "you just sold " + notification.amount + " " + notification.stockName + " stocks for " + notification.price + " per share to "+notification.userName;
        } else {
            message.innerText = "you just bought " + notification.amount + " " + notification.stockName + " stocks for " + notification.price + " per share from "+ notification.userName;
        }
        $("#modal-body").append(message);
        if ( $("#myModal").css('display') === "none") {
            $("#myBtn").trigger('click');
        }
    })
}

function getNotifications()
{
    $.ajax({
            data:"version="+ notificationVersion,
            url:"/userNotifications",
            method:"get",
            timeout:2000,
            error: function(errorObject) {
                console.log("Failed to get movement");
                triggerNotifications();
            },
            success: function(resp) {
                var newResp = JSON.parse(resp);
                if(newResp.version!== notificationVersion)
                {
                    notificationVersion = newResp.version;
                   addToNotificationsModal(newResp);
                }
                triggerNotifications();
            }
        }
    )
}
function triggerNotifications() {
    setTimeout(getNotifications,2000);

}

function setVersionOfNotification() {
    $.ajax({
        url:"/notificationsSecondScreen",
        timeout:2000,
        success(resp)
        {
            if(resp!==notificationVersion) {
                notificationVersion = Math.floor(Number(resp));
            }
            triggerNotifications();
        },
        error(err){
            triggerNotifications();
        }
    })
}

function setNotificationsVersion() {
    setTimeout(setVersionOfNotification,2000)
}

$(function () {
    $("#welcome").text("Welcome "+sessionStorage.getItem("username"));
    setInterval(ajaxUsersList, 2000);
    setInterval(ajaxStocks,3000);
    $("#sumOfMoney").keyup(function ()
    {
        $("#loadMoneyErr").empty();
        if(!isNormalInteger($("#sumOfMoney").val()))
        {
            $('#loadMoneyButton').prop('disabled', true);
            if($("#sumOfMoney").val()!=='')
            $("#loadMoneyErr").append("Money to load should be positive");
        }
        else
        {
            $('#loadMoneyButton').prop('disabled', false);

        }

    })

    setValidationCheckForCreateStock();
    setTimeout(function ()
    {
        var oldBalance = $("#balanceDiv").val();
        $.ajax({
            url: "/balance",
            method:'get',
            timeout: 2000,
            error: function(errorObject) {
                console.log("Failed to fill balance!");
            },
            success: function(resp) {
                $("#sumOfMoney").val("");
                $("#balanceDiv").empty();
                $("#balanceDiv").append("Balance : "+resp);
            }
        });
    },1)



    $("#upload").submit(function() {

        var form = $("#upload")[0];
        var data = new FormData(form);
        $.ajax({
            data: data,
            url: this.action,
            method:'post',
            contentType : false,
            enctype: "multipart/form-data",
            processData: false,
            contentType: false,
            cache: false,
            timeout: 2000,
            error: function(errorObject) {
            },
            success: function(resp) {
                $("#inValidFileError").empty();
                $("#inValidFileError").append(resp);

            }
        });

        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });

    $("#loadMoneyForm").submit(function ()
    {
        $('#loadMoneyButton').prop('disabled', true);
        var oldBalance = $("#balanceDiv").val();
        $.ajax({
        data: $(this).serialize(),
        url: this.action,
        method:'post',
        enctype: "multipart/form-data",
        timeout: 2000,
        error: function(errorObject) {
            console.log("Failed to update balance!");
        },
        success: function(resp) {
            $("#sumOfMoney").val("");
            $("#balanceDiv").empty();
            $("#balanceDiv").append("Balance : "+resp);
        }
    });
    return false;
});


    $("#NewStock").submit(function (){

        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            method:'post',
            timeout: 2000,
            error: function(errorObject) {
                console.log("Failed to create new Stock");
            },
            success: function(resp) {
                if(resp === "false")
                {
                    $("#worthErr").append("Stock already exists in system");
                }
                else {
                    $("#worthErr").append("Stock uploaded");
                }
            }
        });
        $("#createNewStockDiv input[type=text]").val('');
        return false;
    });
    triggerMovement();
   setNotificationsVersion();

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
        $("#modal-body").empty();
    }

// When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
            $("#modal-body").empty();
        }
    }
});

function triggerMovement() {
    setTimeout(getMovements,2000);
}


function getMovements() {
    $.ajax({
            data:"version="+ version,
            url:"/userMovements",
            method:"get",
            timeout:2000,
        error: function(errorObject) {
            console.log("Failed to get movement");
           triggerMovement();
        },
        success: function(resp) {
            var newResp = JSON.parse(resp);
                if(newResp.version!==version)
                {
                    version = newResp.version;
                    addToMovementTable(newResp);
                }
                   triggerMovement()
             }
        }
    )
}

function addToMovementTable(newResp) {
    $.each(newResp.movements || [], function(index, movement) {
        var newBalance = movement.balanceAfter;
        var oldBalance = movement.balanceBefore;
        var sum = newBalance - oldBalance;
      var toInsert=  $('<tr><td>' + movement.kind + '</td>' +
            '<td>'+ movement.symbol +'</td>' +
            '<td>' + movement.date + '</td>' +
            '<td>' + sum + '</td>' +
            '<td>' + oldBalance + '</td>' +
            '<td>' + newBalance + '</td></tr>');

/*
         $("#movementBody").prepend(toInsert);

 */
        $('#accountMovementsTable tr').eq(1).after(toInsert);
        $("#sumOfMoney").val("");
        $("#balanceDiv").empty();
        $("#balanceDiv").append("Balance : "+newBalance);
    })

}
function ajaxUsersList() {
    $.ajax({
        url: "/usersList",
        success: function(users) {
            refreshUsersList(users);
        }
    });
}
function refreshUsersList(users) {
    //clear all current users
    $("#userslist").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(index, username) {
        console.log("Adding user #" + index + ": " + username);

        //create a new <li> tag with a value in it and append it to the #userslist (div with id=userslist) element
        $('<li>' + username + '</li>').appendTo($("#userslist"));
    });
}

function ajaxStocks() {
    $.ajax({
        url: "/stocks",
        success: function(stocks){
            var arrayStock =JSON.parse(stocks);
            $("#allStocksTable").find("tr:gt(1)").remove();
            for(let i = 0; i < arrayStock.length; i++)
            {
                addStockToTable(arrayStock[i]);
            }
        }
    });


    function addStockToTable(stock) {
      var newRow = $('<tr><td>' + stock.stockHolder + '</td>' +
            '<td>'+stock.stockName +'</td>'+
            '<td>'+stock.currentPrice+'</td>'+
            '<td>'+stock.transactionsWorth+'</td>'+
            '</tr>');
      newRow.addClass("goodRow");
      newRow.appendTo($("#allStocksTable"));
      newRow.click(function ()
      {
          version = 0;
          goToStockPage($(this).find('td')[1].innerText);
      })
    }

    function goToStockPage(symbol) {
        sessionStorage.removeItem("symbol");
        sessionStorage.setItem("symbol",symbol);
        window.location.assign("http://localhost:8080/pages/stockPage/stockPage.html");
        };

}

