$(function () {
    $('#headnav').on("click","li",function () {
        var _id=$(this).index();

        $(this).addClass('current-menu').siblings().removeClass('current-menu');
        $('#leftmenu').find('.leftmenu-item').eq(_id).css('display','block')
            .siblings('.leftmenu-item').css('display','none');
    });
    $('#leftmenu').on('click',"dl dt",function(){
        if( $(this).siblings('dd').is(':visible')==false){
            $(this).css('background-position','right 12px');
        }
        else{
            $(this).css('background-position','right -40px');
        }
        $(this).siblings('dd').slideToggle('fast').siblings('dt');

    });
    // 主导航与左侧关联
    // 左侧菜单的切换
    $('#leftmenu').on('click','dl dd ul li a',function () {
        var link=$(this).attr('data-link');
        $('#main').attr('src',link);
        $(this).addClass('current-menuleft').parent().siblings().children().removeClass('current-menuleft');
        $(this).parents('dl').siblings().find('dd ul li a').removeClass('current-menuleft');
    });

    /*获取所有的权限信息*/
    function getAllPrivilege(){
        //取出当前登录的用户信息
        let postResponse = postAjax("../user/getMenuListByUid");
        //查询出权限
        let allPrivilegeList=postResponse.data.result;
        createMenuByData($("#leftmenu_0"),allPrivilegeList);
        $("#main").attr("src","../"+ $(".dashboard").attr("data-url"))
    }
    getAllPrivilege();
    //渲染到页面里面
    function createMenuByData(target,allPrivilegeList){

        target.empty();

        let firstMenus=[];

        let secondMenus=[];

        $.each(allPrivilegeList,function(idx,item){
            //有父
            if(item.pid){
                secondMenus.push(item);
            }else{
                firstMenus.push(item);
            }
        })

        $.each(firstMenus,function(idx,item){
            if("0-0-0"===item.showIndex){
                let $dl=$("<dl id='"+item.id+"' class='dashboard' data-url='"+item.url+"'><dt>"+item.name+"</dt></dl>");
                target.append($dl);
                return ;
            }
            let $dl=$("<dl id='"+item.id+"'><dt>"+item.name+"</dt></dl>");
            let $dd=$("<dd id='"+item.id+"'></dd>");
            let $ul=$('<ul id="'+item.id+'" class="clearfix"></ul>');
            $dd.append($ul);
            $dl.append($dd);
            $.each(secondMenus,function(idx1,item1){
                if(item1.pid==item.id){
                    var $li=$('<li id="'+item1.id+'"><a href="javascript:void(0);" data-link="../'+item1.url+'">'+item1.name+'</a></li>');
                    $ul.append($li);
                }
            })
            target.append($dl);

        })
    }
    $(".dashboard").bind("click",function(){
        $("#main").attr("src","../"+ $(".dashboard").attr("data-url"))
    })
});

