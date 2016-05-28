var ChannelType = {
	NEW_USER: "/user",
	NEW_MESSAGE: "/message",
	OFFLINE: "/offline"
};
var serverIp = "localhost";

var page = {
		
	stompClient: null,
	
	$msgList: $("#lstMsg"),
	$msgModel: $("#msgModel").detach().removeAttr("id"),
	
	$userList: $("#lstUsers"),
	$userModel: $("#userModel").detach().removeAttr("id"),
	
	init: function() {
		page.initView();
		page.bindEvent();
	},
	
	initView: function() {
		
	},
	
	connect: function(userId, successCallback) {
		   var socket = new SockJS(sprintf("http://%s:8080/socket", serverIp));
		   page.stompClient = Stomp.over(socket);
           
			var headers = {
        	    "passcode" : "343",
        	    "login": userId
			}; 
			
           
			//stompClient.debug = null;
           
			page.stompClient.connect(headers, function(response) {
          		successCallback(response);
			
			}, function(disconnectedMessage){
				console.log(disconnectedMessage);
				$(".loginDiv").show();
				$(".container").hide();
			}); 
	},
	
	bindEvent: function() {
		
		$("#txtName").keyup(function(e){
		    if(e.keyCode == 13)
		    {
		    	$("#btnJoin").click();
		    }
		});
		$("#btnJoin").on("click", function() {
			var userId = $("#txtName").val();
			
			page.connect(userId, function(response){
				
				$(".loginDiv").hide();
				$(".container").show();
				
				var users = JSON.parse(response.body);
				$.each(users, function(index, user){
					page.renderUser(user);
				});
				
				// emphasize current user
				page.$userList.find("#" + userId).find("h5").css("font-weight", "bold").css("font-style", "italic")
				
				var channel = "/topic/chat".concat(ChannelType.NEW_MESSAGE);
				page.stompClient.subscribe(channel, function(response){
                	var msg = JSON.parse(response.body);
                	page.renderMessage(msg)
            	});
				
				
				var channel = "/topic/chat".concat(ChannelType.NEW_USER);
				page.stompClient.subscribe(channel, function(response){
                	var user = JSON.parse(response.body);
                	page.renderUser(user);
                	console.log(user);
            	});
				
				var channel = "/topic/chat".concat(ChannelType.OFFLINE);
				page.stompClient.subscribe(channel, function(response){
                	var user = JSON.parse(response.body);
                	page.removeUser(user);
            	});
			})
		});
		
		
		
		$("#txtMessage").keyup(function(e){
		    if(e.keyCode == 13)
		    {
		    	$("#btnSend").click();
		    }
		});
		
		$("#btnSend").on("click", function(){
			var $txtMessage = $("#txtMessage");
			var msg = $txtMessage.val();
			$txtMessage.val("");
			page.stompClient.send("/app/chat", {}, JSON.stringify({"message": msg}));
		});
	},
	
	renderMessage: function(msg) {
		var $msgModel = page.$msgModel.clone();
    	$msgModel.find(".lblMsg").text(msg.message);
    	$msgModel.find(".lblUser").text(msg.sender);
    	page.$msgList.append($msgModel);
	},
	
	renderUser: function(user) {
		var $userModel = page.$userModel.clone();
		$userModel.prop("id", user.userId);
		$userModel.find(".lblUser").text(user.userId);
    	page.$userList.append($userModel);
	},
	
	removeUser: function(user) {
		page.$userList.find("#" + user.userId).remove();
	}
};

$(document).ready(function(){
	page.init();
	
	window.onbeforeunload = function (event) {
		
		// demonstrate before closing browser, 
		// no need to call this, disconnect is called when tab/browser is close
		if(page.stompClient != null) {
			page.stompClient.disconnect();
		}
	};

})