/**
 * Created by apple on 25.11.2014.
 */

var ajaxHelper = {
  setDeleteLinks : function(controllerMethodPath) {
      var links = $('.js-delete-link');
      for (var i = 0; i < links.length; i++) {
          var id = $(links[i]).attr('data-id');
          $(links[i]).click({id: id}, function (e) {
              var messageField = $('.js-alerts');
              messageField.children().remove();
              $.ajax({
                  type: 'GET',
                  url: controllerMethodPath + '/' + e.data.id + '.json',
                  success: function (response) {
                      if (response.status == 'SUCCESS') {
                          var row = $('tr[data-id="' + e.data.id + '"]');
                          row.remove();
                      } else {
                          var messageField = $('.js-alerts');
                          var message = '<div class="alert alert-danger">';
                          for (var k = 0; k < response.errorMessageList.length; k++) {
                              var item = response.errorMessageList[k];
                              message += item.message + '<br/>';
                          }
                          messageField.append(message + '</div>');
                      }
                  }
              });
              e.preventDefault();
          });
      }
  }
};