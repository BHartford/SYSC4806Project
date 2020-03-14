
$(document).ready(function(){
    if (!localStorage.getItem("cart")) {
    	localStorage.cart = [];
    }
    $('.viewCart')[0].innerHTML = "View Cart (" + localStorage.getItem("cart").split(',').filter(function(value, index, arr){ return value !== "";}).length + ")";
    $('.viewCart').click(function(){
        window.location.href = '/cart?books=' + localStorage.getItem("cart");
    });
    $('.addToCart').click(function(){
        cart = localStorage.getItem("cart").split(',').filter(function(value, index, arr){ return value !== "";});
        cart.push(this.id);
        cart = [...new Set(cart)]
        alert(cart);
        localStorage.setItem("cart", cart);
        $('.viewCart')[0].innerHTML = "View Cart (" + cart.length + ")";
    });
});
