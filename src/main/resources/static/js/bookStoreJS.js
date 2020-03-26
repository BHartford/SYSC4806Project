$(document).ready(function () {
    if (!localStorage.getItem("newCart")) {
        localStorage.newCart = JSON.stringify({});
    }


    if ($('#viewCart').length > 0) {
        $('#viewCart').text("View Cart (" + Object.keys(JSON.parse(localStorage.getItem("newCart"))).length + ")");
        $('#viewCart').click(function () {
            if (Object.keys(JSON.parse(localStorage.getItem("newCart"))).length > 0) {
                window.location.href = '/cart?books=' + Object.keys(JSON.parse(localStorage.getItem("newCart"))) +
                    '&quantities=' + Object.values(JSON.parse(localStorage.getItem("newCart")));
            } else {
                alert("Your cart is empty");
            }
        });


    }

    if ($('.addToCart').length > 0) {
        $('.addToCart').click(function () {
            cart = JSON.parse(localStorage.getItem("newCart"));
            let id = this.id;
            if(cart[id]){
                cart[id] = cart[id] + 1;
            }
            else{
                cart[id] = 1;
            }
            localStorage.setItem("newCart", JSON.stringify(cart));
            $('#viewCart').text("View Cart (" + Object.keys(JSON.parse(localStorage.getItem("newCart"))).length + ")");
        });
    }

    if ($('.removeBook').length > 0) {
        $('.removeBook').click(function () {
            var id = this.id;
            var cart = JSON.parse(localStorage.getItem("newCart"));
            delete cart[id];
            localStorage.setItem("newCart", JSON.stringify(cart));
            if (Object.keys(JSON.parse(localStorage.getItem("newCart"))).length > 0) {
                window.location.href = '/cart?books=' + Object.keys(JSON.parse(localStorage.getItem("newCart"))) + '&quantities='
                    + Object.values(JSON.parse(localStorage.getItem("newCart")));
            } else {
                window.location.href = '/';
            }
        });
    }

    if ($('.quantitySelector').length > 0) {
        var cart = JSON.parse(localStorage.getItem("newCart"));
        var loadedQuantities = Object.values(cart);
        var list = document.getElementsByClassName("quantitySelector");
        for (var i = 0; i < list.length; i++) {
            list[i].value = loadedQuantities[i];
        }
        $('.quantitySelector').change(function () {
            var key = Object.keys(cart)[this.id.split(' ')[1]];
            cart[key] =  this.value;
            localStorage.setItem("newCart", JSON.stringify(cart));
            window.location.href = '/cart?books=' + Object.keys(JSON.parse(localStorage.getItem("newCart"))) + '&quantities='
                + Object.values(JSON.parse(localStorage.getItem("newCart")));
        });
    }

    if ($('.login').length > 0) {
        if (localStorage.getItem("user")) {
            $('#logInButton')[0].innerHTML = "Log Out";
        }
        $('.login').click(function () {
            if ($('#logInButton')[0].innerHTML == "Log In") {
                document.getElementById("loginPopup").style.display = "block";

            } else {
                $('#logInButton')[0].innerHTML = "Log In";
                if ($('#addBookButton').length > 0) {
                    $('#addBookButton')[0].style.display = "none";
                }
                if (localStorage.getItem("user")) {
                    localStorage.removeItem("user");
                    localStorage.removeItem("userType");
                }
            }
        });
    }

    if ($('#addBookButton').length > 0) {
        if (localStorage.getItem("userType") && localStorage.getItem("userType") === "Seller") {
            $('#addBookButton')[0].style.display = "block";
        } else {
            $('#addBookButton')[0].style.display = "none";
        }
    }

    if ($('.loginCancel').length > 0) {
        $('.loginCancel').click(function () {
            document.getElementById("loginPopup").style.display = "none";
        });
    }

    if ($('.loginSubmit').length > 0) {
        $('.loginSubmit').click(function () {
            $.ajax({
                type: "POST",
                url: "checkLogin",
                contentType: 'application/json',
                data: JSON.stringify({
                    username: $("#username")[0].value,
                    password: $("#psw")[0].value
                }),
                success: function (data, status, xhr) {
                    if (data.result) {
                        localStorage.user = $("#username")[0].value;
                        localStorage.type = data.type;
                        localStorage.userID = data.userID;
                        document.getElementById("loginPopup").style.display = "none";
                        $('#logInButton')[0].innerHTML = "Log Out";
                        if (data.type === "Seller") {
                            $('#addBookButton')[0].style.display = "block";
                        } else if ($('#addBookButton').length > 0) {
                            $('#addBookButton')[0].style.display = "none";
                        }
                    } else {
                        $("#placeholder")[0].innerHTML = "Invalid Username or Password";
                        $('#addBookButton')[0].style.display = "none";
                    }
                }
            })
        });
    }

    if ($('.purchase').length > 0) {
        $('.purchase').click(function () {
            if (localStorage.getItem("user") === null) {
                alert("Please Login!");
            } else {
                $.ajax({
                    type: "POST",
                    url: "purchaseCart",
                    contentType: 'application/json',
                    data: JSON.stringify({
                        username: localStorage.getItem("user"),
                        cart: Object.keys(JSON.parse(localStorage.getItem("newCart"))).toString(),
                        quantities: Object.values(JSON.parse(localStorage.getItem("newCart"))).toString()
                    }),
                    success: function (data, status, xhr) {
                        localStorage.setItem("newCart", JSON.stringify({}));
                        alert("Successfully purchased " + data.result + " book(s)");
                        window.location = '/viewReceiptHistory?user=' + data.user;
                    }
                })
            }
        });
    }
    if ($('#viewPurchaseHistory').length > 0) {
        $('#viewPurchaseHistory').click(function () {
            if (localStorage.getItem("user") === null) {
                alert("Please Login!");
            } else {
                window.location.href = '/viewReceiptHistory?user=' + localStorage.getItem("userID");
            }
        });
    }
});
