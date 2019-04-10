/**
 * Created by Lynn on 2017/3/18.
 */
$(function () {
    var probeNum=0;

    var intimeCharts=echarts.init(document.getElementById('intimeChart'));

    // var url='/api/intimeData1';
    var url='/frontend/details/intime';
    var today=new Date();
    var todayStr=today.toString();

    var params={
        probeID:probeNum,
        date:todayStr
    };
    getData(params);
    function getData(params) {
        $.get(url,params,function (json) {
            // var jsonParsed=json;
            var jsonStr=json;
            var jsonParsed=eval('('+jsonStr+')');

            intimeCharts.setOption({
                series:[{
                    data:jsonParsed.f1
                },{
                    data:jsonParsed.f2
                },{
                    data:jsonParsed.f3
                },{
                    data:jsonParsed.f4
                },{
                    data:jsonParsed.f5
                }]
            })
        })
    }
    $('#datePicker').datepicker();
    $('.probeID').click(function () {
        $(".probeID").removeClass("chosen");
        $(this).addClass("chosen");
        if($('#probeA').hasClass('chosen')){
            probeNum=0;
        }else{
            probeNum=1;
        }
        params={
            probeID:probeNum,
            date:todayStr
        };
        getData(params);
    });

    $('.searchDate').click(function () {
        // alert($('#datePicker').datepicker('getDate'));
        var dateStr=$('#datePicker').datepicker('getDate').toString();
        params={
            probeID:probeNum,
            date:dateStr
        };
        getData(params);

    });

    var option = {
        title: {
            text: '驻店时长'
        },
        tooltip : {
            trigger: 'axis'
        },
        legend: {
            data:['≤30s', '30s~1min', '1~2min', '2~3min', '>3min']
        },
        toolbox: {
            show : true,
            feature : {
                dataView : {show: true, readOnly: false},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        grid: {
            top:100,
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : ['1:00','2:00','3:00','4:00','5:00','6:00','7:00','8:00','9:00','10:00','11:00','12:00','13:00','14:00','15:00','16:00','17:00','18:00','19:00','20:00','21:00','22:00','23:00','24:00']
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'≤30s',
                type:'line',
                smooth:'true',
                stack: '总量',
                areaStyle: {normal: {}},
                data:[0, 0, 0, 0, 0, 0,0, 132, 151, 204, 250, 230,260, 251, 200, 180, 160, 145,120, 100, 80, 0, 0, 0]
            },
            {
                name:'30s~1min',
                type:'line',
                smooth:'true',
                stack: '总量',
                areaStyle: {normal: {}},
                data:[0, 0, 0, 0, 0, 0,0, 132, 151, 204, 250, 310,320, 282, 241, 214, 190, 170,150, 132, 101, 0, 0, 0]
            },
            {
                name:'1~2min',
                type:'line',
                smooth:'true',
                stack: '总量',
                areaStyle: {normal: {}},

                data:[0, 0, 0, 0, 0, 0,0, 132, 151, 204, 250, 310,320, 282, 241, 214, 190, 170,150, 132, 101, 0, 0, 0]
            },
            {
                name:'2~3min',
                type:'line',
                smooth:'true',
                stack: '总量',
                areaStyle: {normal: {}},

                data:[0, 0, 0, 0, 0, 0,0, 110, 120, 130, 140, 200,187, 197, 132, 155, 131, 98,54, 42, 24, 0, 0, 0]
            },
            {
                name:'>3min',
                type:'line',
                stack: '总量',
                smooth:'true',
                label: {
                    normal: {
                        show: true,
                        position: 'top'
                    }
                },
                areaStyle: {normal: {}},
                data:[0, 0, 0, 0, 0, 0,0, 232, 291, 304, 350, 410,415, 396, 381, 368, 347, 330,321, 296, 272, 0, 0, 0]
            }
        ]
    };

    intimeCharts.setOption(option);
    window.addEventListener("resize", function () {

        intimeCharts.resize();

    });

});

// var option = {
//     title : {
//         text: '驻店时长环比',
//     },
//     tooltip : {
//         trigger: 'axis'
//     },
//     legend: {
//         data:['昨天','今天']
//     },
//     toolbox: {
//         show : true,
//         feature : {
//             dataView : {show: true, readOnly: false},
//             magicType : {show: true, type: ['line', 'bar']},
//             restore : {show: true},
//             saveAsImage : {show: true}
//         }
//     },
//     calculable : true,
//     xAxis : [
//         {
//             type : 'category',
//             data: ['≤1min', '1~3min', '3~5min', '5~10min', '>10min'],
//         }
//     ],
//     yAxis : [
//         {
//             type : 'value'
//         }
//     ],
//     series : [
//         {
//             name:'昨天',
//             barGap:'1%',
//             type:'bar',
//             data:[7.0,  25.6, 76.7,  102.2, 32.6],
//             markPoint : {
//                 data : [
//                     {type : 'max', name: '最大值'},
//                     {type : 'min', name: '最小值'}
//                 ]
//             },
//             markLine : {
//                 data : [
//                     {type : 'average', name: '平均值'}
//                 ]
//             }
//         },
//         {
//             name:'今天',
//             type:'bar',
//             barGap:'1%',
//             data:[26.4,  70.7, 105.6, 48.7, 18.8],
//             markPoint : {
//                 data : [
//                     {type : 'max', name: '最大值'},
//                     {type : 'min', name: '最小值'}
//                 ]
//             },
//             markLine : {
//                 data : [
//                     {type : 'average', name : '平均值'}
//                 ]
//             }
//         }
//     ]
// };