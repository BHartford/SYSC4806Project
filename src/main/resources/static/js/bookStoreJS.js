
$(document).ready(function(){
    if (!localStorage.getItem("cart")) {
    	localStorage.cart = [];
    	localStorage.quantities = [];
    }

    
    if ($('#viewCart').length > 0) {
		$('#viewCart').innerHTML = "View Cart (" + localStorage.getItem("cart").split(',').filter(function(value, index, arr){ return value !== "";}).length + ")";

		$('#viewCart').click(function(){
		    	window.location.href = '/cart?books=' + localStorage.getItem("cart") + '&quantities=' + localStorage.getItem("quantities");
		});
		
		
    }
    
    if ($('.addToCart').length > 0) {
		$('.addToCart').click(function(){
			cart = localStorage.getItem("cart").split(',').filter(function(value, index, arr){ return value !== "";});
			quantities = localStorage.getItem("quantities").split(',').filter(function(value, index, arr){ return value !== "";});
			var index = -1;
			
			for(i=0; i < cart.length; i++){
				if(parseInt(cart[i]) == parseInt(this.id)){
					index = i;
				}
			}
			if(index == -1){
				cart.push(this.id);
				quantities.push(1);				
			} else {
				quantities[index] = parseInt(quantities[index]) + 1;
			}
			localStorage.setItem("cart", cart);
			localStorage.setItem("quantities", quantities);
			$('#viewCart').innerHTML = "View Cart (" + cart.length + ")";
			
			console.log(cart);
			console.log(quantities);
		});
    }
    
    if ($('.removeBook').length > 0) {
		$('.removeBook').click(function(){
			var id = this.id;
			cart = localStorage.getItem("cart").split(',').filter(function(value, index, arr){
				return (value !== "" && value !== id.toString());
			});
			localStorage.setItem("cart", cart);
			window.location.href = '/cart?books=' + localStorage.getItem("cart");
		});
    }

    if ($('.login').length > 0) {
    	if (localStorage.getItem("user")) {
    		$('#logInButton')[0].innerHTML = "Log Out";
    	}
	    $('.login').click(function(){
	    	if ($('#logInButton')[0].innerHTML == "Log In") {
	    		document.getElementById("loginPopup").style.display="block";
	
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
	    $('.loginCancel').click(function(){
	    	document.getElementById("loginPopup").style.display= "none";
	    });
    }
    
    if ($('.loginSubmit').length > 0) {
	    $('.loginSubmit').click(function(){
		    	$.ajax({
		    		type: "POST",
		    		url: "checkLogin",
		    		contentType: 'application/json',
		    		data: JSON.stringify({ 
		    			username: $("#username")[0].value,
		    			password: $("#psw")[0].value
		    			}),
		    		success: function(data, status, xhr) {
		    			if (data.result) {
		    				localStorage.user = $("#username")[0].value;
		    				localStorage.type = data.type;
		    			    document.getElementById("loginPopup").style.display= "none";
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
	    $('.purchase').click(function(){
	        if (localStorage.getItem("user") === null) {
	        	alert("Please Login!");
	        } else {
				$.ajax({
		    		type: "POST",
		    		url: "purchaseCart",
		    		contentType: 'application/json',
		    		data: JSON.stringify({ 
		    			username: $("#username")[0].value,
		    			cart: localStorage.getItem("cart"),
		    			quantities: localStorage.getItem("quantities")
		    			}),
		    		success: function(data, status, xhr) {
	    				alert("Successfully purchased " + data.result + " book(s)");
	    				window.location.href = '/viewPurchaseHistory?user=' + localStorage.getItem("user");
	    				localStorage.cart = [];
		    		}
		    	})
				
				
	        }
	    });
    }
});
