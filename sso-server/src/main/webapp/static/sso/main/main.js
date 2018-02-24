var mainPlatform = {
    init: function(){
        //初始化顶部菜单
        $.ajax({
            url: base_url + '/getMenuTree',
            method: 'get',
            dataType: 'json',
            success: function (res) {
            	var re;
            	if(res.code==200){
            		re=res.data
            	}else{
            		alert("菜单加载异常!")
            		return;
            	}
                var $container = $('#top-level-container');
                $.each(re, function (i, v) {
                    if(v.name != "首页"){
                        var $li = $('<li class="pf-nav-item home"></li>');
                        var $a = $('<a href="javascript:;"></a>');
                        var $span1 = $('<span class="iconfont icon-'+v.icon+'"></span>');
                        var $span = $('<span class="pf-nav-title"></span>').append(v.name);
                        $li.data("data", v);
                        $container.append($li.append($a.append($span1).append($span)));
                    }
                });
                ///-----------------------顶部翻页
                var page = 0,
                    pages = ($('#top-level-container').height() / 70) - 1;
                if (pages === 0) {
                    $('.pf-nav-prev,.pf-nav-next').hide();
                }
                $(document).on('click', '.pf-nav-prev,.pf-nav-next', function () {
                    if ($(this).hasClass('disabled')) return;
                    if ($(this).hasClass('pf-nav-next')) {
                        page++;
                        $('.pf-nav').stop().animate({'margin-top': -70 * page}, 200);
                        if (page == pages) {
                            $(this).addClass('disabled');
                            $('.pf-nav-prev').removeClass('disabled');
                        } else {
                            $('.pf-nav-prev').removeClass('disabled');
                        }
                    } else {
                        page--;
                        $('.pf-nav').stop().animate({'margin-top': -70 * page}, 200);
                        if (page == 0) {
                            $(this).addClass('disabled');
                            $('.pf-nav-next').removeClass('disabled');
                        } else {
                            $('.pf-nav-next').removeClass('disabled');
                        }
                    }
                });
	            $('#pf-hd .pf-nav li').first().click();
            }
        });
        this.bindEvent();
    },
    bindEvent: function(){
        // 顶部大菜单单击事件
        $(document).on('click', '.pf-nav-item', function() {
            // 切换选中状态
            if($(this).hasClass('current'))
                return false;
            $('.pf-nav-item').removeClass('current');
            $(this).addClass('current');
            // 判断是否有子菜单
            var data = $(this).data('data');
            if(data.children.length === 0){
                // 没有子菜单
                alert("没有子菜单");
            }else{
                // 加载左侧菜单
                // 清空左侧菜单
                var $container = $("#pf-sider").html('');
                $container.append('<h2 class="pf-model-name"><span class="iconfont icon-'+data.icon+'"></span><span class="pf-name">' + data.name + '</span> <span class="toggle-icon"></span></h2>')
                var $ul = $('<ul class="sider-nav"></ul>').html('');
                $.each(data.children, function (i, v) {
                    // 判断是否有子菜单
                    if(!v.children){
                        // 没有子菜单
                        var $a = $('<a href="javascript:;"></a>').on('click', function () {
                            addTab(v.name, v.url);
                        });
                        $ul.append($('<li></li>').append($a.append('<span class="iconfont icon-'+v.icon+' sider-nav-icon"></span><span class="sider-nav-title">' + v.name + '')));
                    }else{
                        // 有子菜单
                        var $li = $('<li></li>');
                        var $a = $('<a href="javascript:;"><span class="iconfont icon-'+v.icon+' sider-nav-icon"></span><span class="sider-nav-title">' + v.name + '</span><i class="iconfont">&#xe642;</i></a>');
                        var $ul1 = $('<ul class="sider-nav-s" ></ul>');
                        $.each(v.children, function (i1, v1) {
                            $ul1.append($('<li></li>').append(
                                $("<a href='javascript:;'>" + v1.name + "</a>").on('click', function () {
                                    addTab(v1.name, v1.url);
                                })
                            ));
                        });
                        $ul.append($li.append($a).append($ul1));
                    }
                });
                $container.append($ul);
            }
//            $('.sider-nav li').first().trigger('click')
        });
        
        $(document).on('click', '.sider-nav li', function() {
            $('.sider-nav li').removeClass('current');
            $(this).addClass('current');
//            $('iframe').attr('src', $(this).data('src'));
        });

        $(document).on('click', '.pf-logout', function () {
            $.messager.confirm({
                title: '提示',
                msg: '确定要退出吗？',
                draggable: false,
                fn: function (isTrue) {
                    if (isTrue)
                        window.location = base_url + "/loginout";
                }
            });
        });

        //左侧菜单收起
        $(document).on('click', '.toggle-icon', function() {
            $(this).closest("#pf-bd").toggleClass("toggle");
            setTimeout(function(){
                $(window).resize();
            },300)
        });
        
        //修改密码
        $(document).on('click', '.pf-modify-pwd', function() {
        	$("#cg_password").dialog("setTitle","修改密码").dialog("open").dialog("center");
        });

        $(document).on('click', '.pf-notice-item', function() {
            $('#pf-page').find('iframe').eq(0).attr('src', 'backend/notice.html')
        });
    }
};

function changePassword(){
	$("#passwordForm").form("submit", {  
        url: base_url+'/changePwd',
        onsubmit: function () {  
            return $(this).form("validate");  
        },  
        success: function (res) {
        	res = JSON.parse(res)
        	if(res.code==200){
        		$("#cg_password").dialog('close');
        	}
        	msg(res.msg)
       }  
    });
};
mainPlatform.init();

