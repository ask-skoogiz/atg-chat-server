(function () {
  'use strict';

  angular
  .module('app')
  .controller('Home.IndexController', Controller);

  function Controller($q, $scope, $localStorage, $timeout, ChatService) {

    var vm = this;

    $scope.users = [];

    $scope.messages = [];

    $scope.status = 0;

    $scope.msg = "";

    $scope.selectedContact = "";

    $scope.ongoingChats = {};

    $scope.selectContact = function (contact) {
      $scope.selectedContact = contact;
      ChatService.getChat(vm.user, contact, function (success, data) {
        if (success) {
          if (!$scope.ongoingChats.hasOwnProperty(contact)) {
            addChat(contact, data);
          }

          $scope.messages = $scope.ongoingChats[contact].messages;

          console.log("getChat: " + data.chat_id);
          console.log("getChat: " + data.messages);
        } else {
          console.log("getChat: failed!");
        }
      });
    };

    function addChat(contact, chat) {
      $scope.ongoingChats[contact] = {
        chat_id: chat.chat_id,
        messages: chat.messages
      };
    }

    $scope.onEnter = function ($event) {
      if ($event.keyCode === 13) {
        $scope.send();
      }
    };

    $scope.send = function() {
      if ($scope.msg !== "" && $scope.selectedContact !== "") {
        var promise = ChatService.sendMessage({
          from: vm.user,
          to: $scope.selectedContact,
          text: $scope.msg,
          chat_id: $scope.ongoingChats[$scope.selectedContact].chat_id
        });
        promise.then(function(data) {
          $timeout(function() {
            addMessage(data.message);
          }, 0);

        });
        $scope.msg = "";
      }
    }

    vm.onConnect = $q.defer();
    vm.onConnect.promise.then(function () {
      status()
      console.debug("onConnect");
    });

    vm.onDisconnect = $q.defer();
    vm.onDisconnect.promise.then(function () {
      status()
      console.debug("onDisconnect");
    });

    vm.info = function(data) {
      console.debug("run info")
      if (data.type === "server_info" && data.userlist) {
        $scope.$apply(function () {
          $scope.users = data.userlist;
        });
      }
      if (data.type === "send_message") {
        addMessage(data.message);
      }
    };

    initController();

    function addMessage(message) {

      //  Check that messages goes to the right people.

      if ($scope.selectedContact === message.to || $scope.selectedContact === message.from) {
        $scope.messages.unshift(message);
      } else if ($scope.ongoingChats.hasOwnProperty(message.from)) {
        $scope.ongoingChats[message.from].messages.unshift(message);
      } else if ($scope.ongoingChats.hasOwnProperty(message.to)) {
        $scope.ongoingChats[message.to].messages.unshift(message);
      }


      $scope.$apply($scope.messages);
    }

    function initController() {
      vm.user = $localStorage.currentUser.username;
      vm.socket = ChatService.connect(vm.onConnect, vm.onDisconnect, vm.info);
      status();
    }

    function status() {
      $scope.status = ChatService.status();
    }

  }
})();
