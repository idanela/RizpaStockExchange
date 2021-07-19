
$(function () {
    $("#welcome").text("Welcome " + sessionStorage.getItem("username"));
    setInterval(ajaxUsersList, 2000);
    setInterval(ajaxStocks, 3000);


    })


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
        window.location.assign("http://localhost:8080/pages/stockPageAdmin/stockPageAdmin.html");
    };

}

