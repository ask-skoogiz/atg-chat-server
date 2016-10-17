(function () {
  'use strict';

  angular
  .module('app')
  .factory('ChatService', Service);

  function Service($q, $rootScope, $http, $location, $localStorage) {

    var vm = this;

    var service = {};

    var callbacks = {};

    $rootScope.$on('$locationChangeStart', function (event, next, current) {
      if (exists() && '/login' === $location.path()) {
        vm.ws.close()
      }
    });

    service.connect = function (onConnect, onDisconnect, onInfo) {
      if (!exists()) {
        websocket(onConnect, onDisconnect);
      } else if (vm.ws.readyState == 2 || vm.ws.readyState == 3) {
        websocket(onConnect, onDisconnect);
      }
      vm.info = onInfo;
      return vm.ws;
    }

    service.status = function () {
      return vm.ws.readyState;
    }

    service.sendMessage = function (message) {
      var request = {
        type: "send_message",
        message: message
      }
      var promise = sendRequest(request);
      return promise;
    }

    service.getChat = getChat;
    
    function getChat(from, to, callback) {
        $http.get("/api/getChat/"+from+"/"+to)
            .success(function (response) {
                if (response.chat_id) {
                    callback(true, response);
                } else {
                    callback(false);
                }
            });
    }
    
    function sendRequest(request) {
      var defer = $q.defer();
      var callbackId = generateUUID();
      callbacks[callbackId] = {
        time: new Date(),
        cb:defer
      };
      request.callback_id = callbackId;
      console.log('Sending request', request);
      vm.ws.send(JSON.stringify(request));
      return defer.promise;
    }

    function listener(data) {
      var messageObj = data;
      console.log("Received data from websocket: ", messageObj);
      if(callbacks.hasOwnProperty(messageObj.callback_id)) {
        console.log(callbacks[messageObj.callback_id]);
        $rootScope.$apply(callbacks[messageObj.callback_id].cb.resolve(messageObj));
        delete callbacks[messageObj.callbackID];
      } else if (vm.info) {
        vm.info(messageObj);
      }
    }

    function generateUUID() {
      var d = new Date().getTime();
      var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (d + Math.random()*16)%16 | 0;
        d = Math.floor(d/16);
        return (c=='x' ? r : (r&0x3|0x8)).toString(16);
      });
      return uuid;
    };

    function websocket(onConnect, onDisconnect) {
      var ws = new WebSocket("ws://" + $location.host() + ":" + $location.port() + "/socket/chat?user=" + $localStorage.currentUser.username);
      ws.onopen = function(){
        onConnect.resolve();
        console.log("Socket has been opened!");
      };
      ws.onmessage = function(message) {
        listener(JSON.parse(message.data));
      };
      ws.onclose = function(){
        onDisconnect.resolve();
        console.log("Socket has been closed!");
      };
      vm.ws = ws;
    }

    function exists() {
      return (vm.ws ? true : false);
    }

    return service;

  }
})();
