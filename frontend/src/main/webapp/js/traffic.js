/**
 * Created by Lynn on 2017/3/15.
 */
$(function () {
    var probeNum=0;
    var trafficCharts = echarts.init(document.getElementById('trafficChart'));

    // var url = '/api/trafficData';
    var url='/frontend/details/traffic';

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

    var today=new Date();
    var todayStr=today.toString();
    var params={
        probeID:probeNum,
        date:todayStr
    };
    function getData(params) {
        $.get(url,params,function (json) {
            // var jsonParsed = json;
            var jsonStr=json;
            var jsonParsed=eval('('+jsonStr+')');

            trafficCharts.setOption({
                options: [
                    {
                        series: jsonParsed.d1
                    },
                    {
                        series: jsonParsed.d2
                    },
                    {
                        series: jsonParsed.d3
                    },
                    {
                        series: jsonParsed.d4
                    },
                    {
                        series: jsonParsed.d5
                    },
                    {
                        series: jsonParsed.d6
                    },
                    {
                        series: jsonParsed.d7
                    }
                ]
            });
        });
    }
    getData(params);

    $('#datePicker').datepicker();

    $('.searchDate').click(function () {
        // alert($('#datePicker').datepicker('getDate'));
        var dateStr = $('#datePicker').datepicker('getDate').toString();
        params={
            probeID:probeNum,
            date:dateStr
        };
        getData(params)
    });

    var option = {
        baseOption:{
            timeline: {
                data: [
                    '一', '二', '三', '四', '五', '六', '七'
                ],
                axisType: 'category',
                show: true,
                autoPlay: true,
                playInterval: 2000
            },
            title: {
                text: '探针A客流情况'
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                show: true,
                x: 'right',
                data: ['上客量', '下客量'],
                selected: {
                    '上客量': true,
                    '下客量': true
                }
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
            calculable: true,

            grid: {
                y: 80,
                y2: 100
            },
            xAxis: [{
                type: 'category',
                axisLabel: {
                    interval: 0
                },
                data: [
                    '1：00','2：00','3：00','4：00','5：00', '6：00', '7：00', '8：00', '9：00', '10：00', '11：00', '12：00', '13：00', '14：00', '15：00', '16：00', '17：00', '18：00', '19：00', '20：00', '21：00', '22：00', '23：00','24：00'

                ]
            }],
            yAxis: [
                {
                    type: 'value',
                    name: '人数'
                },
                {
                    type: 'value',
                    name: '入店率',
                    min: 0,
                    max: 1

                }
            ],
            series: [{
                name: '客流量',
                yAxisIndex: 0,
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                },
                markLine : {
                    data : [
                        {type : 'average', name: '平均值'}
                    ]
                },
                barGap:'1%',
                type: 'bar',
                smooth:'true',
                itemStyle: {
                    normal: {

                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: 'rgba(194, 53,49, 1)'
                        }, {
                            offset: 1,
                            color: 'rgba(194, 53,49, 0.5)'
                        }]),
                        shadowColor: 'rgba(0, 0, 0, 0.5)',
                        shadowBlur: 10

                    }
                }
            }, {
                name: '入店量',
                yAxisIndex: 0,
                smooth:'true',
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                },
                markLine : {
                    data : [
                        {type : 'average', name: '平均值'}
                    ]
                },
                type: 'bar',
                itemStyle: {
                    normal: {

                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: 'rgba(97, 160,168, 1)'
                        }, {
                            offset: 1,
                            color: 'rgba(97, 160,168, 0.5)'
                        }]),
                        shadowColor: 'rgba(0, 0, 0, 0.5)',
                        shadowBlur: 10

                    }
                }
            },{
                name: '入店率',
                yAxisIndex: 1,
                smooth:'true',
                type: 'line',
                itemStyle:{
                    normal:{
                        color:'#91c7ae'
                    }
                }
            }]
        },
        options: [
             {
            title: {
                text: '探针A客流情况'
            },
            series: [{
                data: [0, 0, 0, 0,0, 0, 0, 28, 30, 40, 43, 57, 65, 72, 56, 65, 60, 58, 47, 59, 41, 0, 0, 0]
            }, {
                data: [0, 0, 0, 0,0, 0, 0, 26, 28, 38, 40, 55, 61, 69, 53, 60, 57, 54, 44, 54, 34, 0, 0, 0]
            },{
                data:[0,0, 0, 0, 0, 0, 0, 0.9285714285714286,0.9333333333333333,0.95,0.9302325581395349,0.9649122807017544,0.9384615384615385,0.9583333333333334,0.9464285714285714,0.9230769230769231,0.95,0.9310344827586207,0.9361702127659575,0.9152542372881356,0.8292682926829268,0,0,0]
            }]
        }, {
            title: {
                text: '探针A客流情况'
            },
            series: [{
                data: [0, 0, 0, 0,0, 0, 0, 19, 24, 26, 33, 42, 49, 30, 55, 52, 48, 42, 32, 44, 29, 0, 0, 0]
            }, {
                data: [0, 0, 0, 0,0, 0, 0, 13, 20, 22, 29, 37, 46, 25, 48, 46, 45, 40, 30, 41, 25, 0, 0, 0]
            },{
                data:[0,0, 0, 0, 0, 0, 0, 0.6842105263157895,0.8333333333333334,0.8461538461538461,0.8787878787878788,0.8809523809523809,0.9387755102040817,0.8333333333333334,0.8727272727272727,0.8846153846153846,0.9375,0.9523809523809523,0.9375,0.9318181818181818,0.8620689655172413,0,0,0]
            }]
        }, {
            title: {
                text: '探针A客流情况'
            },
            series: [{
                data: [110, 2, 111, 176,110, 2, 111, 176, 73, 59, 181, 9, 0, 86, 83, 6, 5, 11, 5, 7, 1, 28, 28, 62]
            }, {
                data: [64, 7, 39, 32,64, 7, 39, 32, 30, 11, 63, 1, 3, 74, 42, 4, 75, 57, 0, 28, 0, 28, 50, 29]
            },{
                data:[0.60, 0.65, 0.55, 0.45, 0.48, 0.49,  0.48, 0.49, 0.68, 0.74, 0.88, 0.76, 0.70, 0.50,0.68, 0.74, 0.88, 0.76, 0.70, 0.50,0.60, 0.62, 0.55, 0.45]
            }]
        }, {
            title: {
                text: '探针A客流情况'
            },
            series: [{
                'data': [94, 7, 64, 55,94, 7, 64, 55, 56, 41, 70, 1, 0, 35, 44, 2, 17, 25, 8, 18, 9, 60, 52, 87]
            }, {
                'data': [49, 13, 24, 23,49, 13, 24, 23, 11, 3, 28, 4, 11, 24, 61, 7, 159, 65, 2, 48, 4, 69, 51, 86]
            },{
                data:[0.60, 0.65, 0.55, 0.45, 0.48, 0.49, 0.68, 0.74, 0.88, 0.76,  0.55, 0.45, 0.48, 0.49, 0.68, 0.74, 0.70, 0.50,0.60, 0.62,0.88, 0.76, 0.70, 0.50]
            }]
        }, {
            title: {
                text: '探针A客流情况'
            },
            series: [{
                'data': [70, 4, 79, 61,70, 4, 79, 61, 42, 39, 116, 10, 0, 19, 40, 3, 4, 27, 19, 13, 4, 28]
            }, {
                'data': [64, 2, 24, 45,64, 2, 24, 45, 20, 5, 33, 5, 4, 31, 9, 3, 120, 61, 0, 31, 8, 56, 36]
            },{
                data:[0.60, 0.65,  0.74, 0.88, 0.76, 0.70, 0.50,0.60, 0.62, 0.55, 0.45, 0.48, 0.49, 0.68, 0.74,0.55, 0.45, 0.48, 0.49, 0.68, 0.88, 0.76, 0.70, 0.50]
            }]
        }, {
            title: {
                text: '探针A客流情况'
            },
            series: [{
                'data': [29, 4, 70, 44,29, 4, 70, 44, 37, 45, 57, 6, 0, 19, 33, 1, 4, 5, 6, 11, 4, 24, 5, 42]
            }, {
                'data': [39, 6, 44, 20,39, 6, 44, 20, 16, 0, 27, 8, 7, 17, 5, 0, 44, 57, 1, 27, 4, 29, 28, 24]
            },{
                data:[ 0.49, 0.68, 0.74, 0.88, 0.76,0.60, 0.65, 0.55, 0.45, 0.48, 0.70, 0.50,0.60, 0.62, 0.55, 0.45, 0.48, 0.49, 0.68, 0.74, 0.88, 0.76, 0.70, 0.50]
            }]
        }, {
            title: {
                text: '探针A客流情况'
            },
            series: [{
                'data': [14, 2, 85, 52,14, 2, 85, 52, 38, 38, 40, 18, 0, 16, 48, 2, 12, 8, 15, 18, 2, 30, 9, 5]
            }, {
                'data': [29, 5, 38, 35,29, 5, 38, 35, 12, 3, 36, 16, 2, 11, 22, 0, 25, 86, 4, 52, 6, 17, 2, 15]
            },{
                data:[0.60, 0.65, 0.55,0.74, 0.88, 0.76, 0.70, 0.45, 0.48, 0.49, 0.68,  0.50,0.60, 0.62, 0.55, 0.45, 0.48, 0.49, 0.68, 0.74, 0.88, 0.76, 0.70, 0.50]
            }]
        }
        ]
    };

    trafficCharts.setOption(option);
    window.addEventListener("resize", function () {

        trafficCharts.resize();

    });

});
