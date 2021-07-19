var TransactionsVersion = 0;
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
            '<td>'+ transaction.amount+'</td>' +
            '<td>' + transaction.price+ '</td>' +
            '</tr>');
            /*.appendTo("#stockTransactionsTable");*/
        $("#bod").prepend(toInsert);
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
                getStockDetails();
            }
            triggerFillTransactionsTable();
        }
    })
}

function triggerFillTransactionsTable() {
    setTimeout(fillStockTransactions ,2000);
}


function addPendingTransactions(transactions) {
    $.each(transactions || [], function(index, transaction) {
        var toInsert = $('<tr><td>' +transaction.instruction+'</td>>'+
            '<td class="kind">' +transaction.kind+'</td>>'+
            '<td>' + transaction.symbol + '</td>' +
            '<td>'+ transaction.amount +'</td>' +
            '<td>' + transaction.price+ '</td>' +
            '<td>' + transaction.initiator+ '</td>' +
            '</tr>');
     $("#pendingBod").append(toInsert);
/*
        $('#pendingTransactionsTable tr').eq(1).after(toInsert);
*/
/*
        $('#pendingTransactionsTable tbody').append(toInsert);
*/
      })
}

function refreshPendingTransactionsList(transactions) {
    $("#pendingTransactionsTable").empty();
    $("<thead><tr><th class=\"tableHeadline\" colspan=\"6\">\n" +
        "            Pending Transactions" +
        "        </th>" +
        "    </tr>" +
        "    <tr>" +
        "        <th>Instruction</th>" +
        "        <th id='kindTh'>Kind</th>" +
        "        <th>Symbol</th>" +
        "        <th>Amount</th>" +
        "        <th>Price</th>" +
        "        <th>Initiator</th>" +
        "    </tr></thead><tbody id='pendingBod'></tbody>").appendTo($("#pendingTransactionsTable"));
    addPendingTransactions(transactions);
}

function pullPendingTransactions() {
    $.ajax({
        data: "symbol=" + sessionStorage.getItem("symbol"),
        url: "/pullPendingTransactions",
        success: function(resp) {
            var transactions = JSON.parse(resp);
            refreshPendingTransactionsList(transactions);
        }
    });
}

function triggerPullPendingTransactions() {
    setInterval(pullPendingTransactions,2000);
}

$(function () {
    $("#userNameDiv").text("Hello "+sessionStorage.getItem("username"));

    getStockDetails();
    triggerFillTransactionsTable();
    triggerPullPendingTransactions();
});
