;(function () {
	$.extend({
		initHeight: function () {
			$(window).resize(function () {
				var h = $(window).height()-$('.top-header').outerHeight();
				$('.top-body').height(h)
			}).resize();
		}
	})
     $.fn.extend({
        setFile: function (callback) {
            return this.each(function (index, item){
                $(item).wrap('<div class="input-file-warpper"></div>').hide();
                var $thisText =  $('<input type="text" class="input-file-text form-control-150" disabled placeholder="'+item.placeholder+'"/>');
                var $thisBtn = $('<a class="input-file-btn base-input">'+item.title+'</a>');
                $(item).after($thisBtn).after($thisText);
                $thisBtn.on('click', function () {
                    $(item).trigger('click');
                });
                $(item).on('change', function () {
                    var value = this.value.substr(this.value.lastIndexOf('\\')+1);
                    $thisText.val(value);
                    callback && callback()
                })
            })
        }
    })
})()