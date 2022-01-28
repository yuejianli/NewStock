// 引入公共的Js文件
document.writeln('<script type="text/javascript" src="../webjars/jquery/3.5.1/jquery.js"></script>');
document.writeln('<script type="text/javascript" src="../webjars/bootstrap/3.4.1/js/bootstrap.js"></script>');
document.writeln('<script type="text/javascript" src="../static/plugins/bootstrap-table/bootstrap-table.js"></script>');
document.writeln('<script type="text/javascript" src="../static/plugins/bootstrap-select/bootstrap-select.min.js"></script>');
document.writeln('<script type="text/javascript" src="../static/plugins/flavr/flavr.min.js"></script>');
document.writeln('<script type="text/javascript" src="../static/plugins/bootstrap-daterangepicker/moment.min.js"></script>');
document.writeln('<script type="text/javascript" src="../static/plugins/bootstrap-daterangepicker/daterangepicker.js"></script>');
document.writeln('<script type="text/javascript" src="../static/plugins/bootstrap-daterangepicker/daterangepicker.zh-CN.js"></script>');
document.writeln('<script type="text/javascript" src="../static/myjs/myjs.js"></script>');
document.writeln('<script type="text/javascript" src="../static/myjs/myArray.js"></script>');
document.writeln('<script type="text/javascript" src="../static/myjs/commonUrl.js"></script>');
let currentHtmlUseJs=pageName();
document.writeln('<script type="text/javascript" src="'+currentHtmlUseJs+'.js"></script>');


function pageName() {
    var a = location.href;
    var b = a.split("/");
    var c = b.slice(b.length-1, b.length).toString(String).split(".");
    return c.slice(0, 1);
}







