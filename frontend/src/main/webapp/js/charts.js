/**
 * Created by Lynn on 2017/3/3.
 */
// color:['#c23531','#2f4554', '#61a0a8', '#d48265', '#91c7ae','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570', '#c4ccd3']
// var ajaxPre = "http://localhost:8080/frontend";

//size insize inrate

//ajax URL here
var url = {
    lineChart: '/frontend/getFlow',
    intime: '/frontend/getintime',
    T: '/frontend/getactivity',
    No: '/frontend/getNo',
    gps:'/frontend/getgps'

    // lineChart: '/getFlow',
    // intime: '/intime',
    // T: '/period',
    // No: '/newOld',
    // gps: '/api/gps'
};
var probeNum = 0;
var params = {
    probeID: probeNum
};
//时间段
function updateTime() {
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();
    var hour = date.getHours();
    var minute = date.getMinutes();


    var monthAgo = new Date((date - 30 * 24 * 3600 * 1000));
    var dayAgo = new Date((date - 24 * 3600 * 1000));
    var hourAgo = new Date((date - 3600 * 1000));

    var monthStr = monthAgo.getFullYear() + '/' + (monthAgo.getMonth() + 1) + '-' + year + '/' + month;
    var dayStr = (dayAgo.getMonth() + 1) + '/' + dayAgo.getDate() + '-' + month + '/' + day;
    var hourStr = hour+':00 - '+ hour+':' + minute;
    $('#visActivity').next().html("时间段：" + monthStr);
    $('#newOldVisitor').next().html("时间段：" + dayStr);
    $('#jumpVisitor').next().html("时间段：" + hourStr);
    $('#visLastVis').next().html("时间段：" + monthStr);
    $('#timeStay').next().html("时间段：" + hourStr);
}
//地图

$(function () {
    var map = new BMap.Map("probeMap");
    var lon = 116.62;
    var lat = 40.32;
    params = {
        probeID: probeNum
    };
    function newMap() {
        var point = new BMap.Point(lon, lat);
        var convertor = new BMap.Convertor();
        var pointArr = [];
        pointArr.push(point);
        convertor.translate(pointArr, 1, 5, function (data) {


            map.centerAndZoom(data.points[0], 14);

            var marker = new BMap.Marker(data.points[0]);

            map.addOverlay(marker);
        });
        map.enableScrollWheelZoom();
    }

    function getGPS(params) {
        $.get(url.gps, params, function (json) {
            var jsonStr=json;
            var GPSParsed=eval('('+jsonStr+')');
            // var GPSParsed = json;

            lon = GPSParsed.lon;
            lat = GPSParsed.lat;
            
            newMap();
        });
        

    }

    getGPS(params);


    $('#datePicker').datepicker();

    $('.probeID').click(function () {
        $(".probeID").removeClass("chosen");
        $(this).addClass("chosen");
        if ($('#probeA').hasClass('chosen')) {
            probeNum = 0;
        } else {
            probeNum = 1;
        }
        params = {
            probeID: probeNum
        };
        getlineJSON(params);
        getGPS(params);
        setTimeout(function () {
            getTJSON(params);
            setTimeout(function () {
                getNoJSON(params);
                setTimeout(function () {
                    getintimeJSON(params);
                }, 1000);
            }, 1000);
        }, 1000);
    });


});
var keliudata = [];
var visitordata = [];
var visperdata = [];
var vlvdata = [];
var tsdata = [];
var vadata = [];
var nodata = [];
var jidata = [];

var keliucharts = echarts.init(document.getElementById('traffic'));
var visitorcharts = echarts.init(document.getElementById('visitor'));
var vispercharts = echarts.init(document.getElementById('visPer'));
var vlvcharts = echarts.init(document.getElementById('visLastVis'));
var tscharts = echarts.init(document.getElementById('timeStay'));
var vacharts = echarts.init(document.getElementById('visActivity'));
var nocharts = echarts.init(document.getElementById('newOldVisitor'));
var jicharts = echarts.init(document.getElementById('jumpVisitor'));

var jsonlineParsed = [];
var jsonintimeParsed = [];
var jsonTParsed = [];
var jsonNoParsed = [];

function jsontoline(json) {
    var jsonStr=json;
    jsonlineParsed=eval('('+jsonStr+')');
    // jsonlineParsed = json;
    for (var i = 0; i < 10; i++) {
        keliudata.push({
            name: jsonlineParsed[i].time,
            value: [jsonlineParsed[i].time,
                jsonlineParsed[i].size
            ]
        });
        visitordata.push({
            name: jsonlineParsed[i].time,
            value: [jsonlineParsed[i].time,
                jsonlineParsed[i].insize
            ]
        });
        visperdata.push({
            name: jsonlineParsed[i].time,
            value: [jsonlineParsed[i].time,
                jsonlineParsed[i].inrate
            ]
        });
        keliudata.shift();
        visperdata.shift();
        visitordata.shift();
    }
}
function jsontointime(json) {
    var jsonStr=json;
    jsonintimeParsed=eval('('+jsonStr+')');
    // jsonintimeParsed = json;
    tsdata[0] = jsonintimeParsed.f1;
    tsdata[1] = jsonintimeParsed.f2;
    tsdata[2] = jsonintimeParsed.f3;
    tsdata[3] = jsonintimeParsed.f4;
    tsdata[4] = jsonintimeParsed.f5;

    jidata = [{
        name: '深访率',
        value: jsonintimeParsed.deep
    }, {
        name: '跳出率',
        value: jsonintimeParsed.jump
    }, {
        name: '正常客流占比',
        value: (1 - jsonintimeParsed.deep - jsonintimeParsed.jump)
    }];
}
function jsontoT(json) {
    var jsonStr=json;
    jsonTParsed=eval('('+jsonStr+')');
    // jsonTParsed = json;
    vlvdata[0] = jsonTParsed.t1;
    vlvdata[1] = jsonTParsed.t2;
    vlvdata[2] = jsonTParsed.t3;
    vlvdata[3] = jsonTParsed.t4;
    vlvdata[4] = jsonTParsed.t5;
    vlvdata[5] = jsonTParsed.t6;

    vadata = [
        {value: jsonTParsed.high, name: '高活跃度'},
        {value: jsonTParsed.medium, name: '中活跃度'},
        {value: jsonTParsed.low, name: '低活跃度'},
        {value: jsonTParsed.sleep, name: '沉睡客户'}
    ];
}

function jsontoNo(json) {
    var jsonStr=json;
    jsonNoParsed=eval('('+jsonStr+')');
    // jsonNoParsed = json;
    nodata = [
        {value: jsonNoParsed.newVisitor, name: '新顾客'},
        {value: jsonNoParsed.oldVisitor, name: '老顾客'}
    ]
}
function setlineOP() {
    keliucharts.setOption({
        series: [{
            data: keliudata
        }]

    });
    visitorcharts.setOption({
        series: [{
            data: visitordata
        }]
    });
    vispercharts.setOption({
        series: [{
            data: visperdata
        }]
    })
}
function setintimeOP() {
    tscharts.setOption({
        series: [{
            data: tsdata
        }]
    });
    jicharts.setOption({
        series: [{
            data: jidata
        }]
    })
}
function setTOP() {
    vlvcharts.setOption({
        series: [{
            data: vlvdata
        }]
    });
    vacharts.setOption({
        series: [{
            data: vadata
        }]
    });
}

function setNoOP() {
    nocharts.setOption({
        series: [{
            data: nodata
        }]
    })
}

function getlineJSON(params) {
    $.get(url.lineChart, params, function (json) {
        jsontoline(json);
        setlineOP();
    });
}
function getintimeJSON(params) {
    $.get(url.intime, params, function (json) {
        jsontointime(json);
        setintimeOP();
    });
}
function getTJSON(params) {
    $.get(url.T, params, function (json) {
        jsontoT(json);
        setTOP();
    });
}

function getNoJSON(params) {
    $.get(url.No, params, function (json) {
        jsontoNo(json);
        setNoOP();
    })
}

$(function () {
    getlineJSON(params);

    setTimeout(function () {
        getTJSON(params);
        setTimeout(function () {
            getNoJSON(params);
            setTimeout(function () {
                getintimeJSON(params);
            }, 1000);
        }, 1000);
    }, 1000);
    // getintimeJSON();
    // getTJSON();
    // getVAJSON();
    // getNoJSON();
    updateTime();
});

setInterval(function () {
    getlineJSON(params);
}, 3000);

setInterval(function () {
    setTimeout(function () {
        getTJSON(params);
        setTimeout(function () {
            getNoJSON(params);
            setTimeout(function () {
                getintimeJSON(params);
            }, 1000);
        }, 1000);
    }, 1000);
    updateTime();
}, 300000);

$(function () {

    var json1 = [
        {
            time: "2017/03/04 22:22:22",
            size: 100,
            insize: 100,
            inrate: 0.5

        }, {
            time: "2017/03/04 22:22:25",
            size: 100,
            insize: 100,
            inrate: 0.5

        }, {
            time: "2017/03/04 22:22:28",
            size: 100,
            insize: 100,
            inrate: 0.5

        }, {
            time: "2017/03/04 22:22:31",
            size: 100,
            insize: 100,
            inrate: 0.5

        }, {
            time: "2017/03/04 22:22:34",
            size: 100,
            insize: 100,
            inrate: 0.5

        }, {
            time: "2017/03/04 22:22:37",
            size: 100,
            insize: 100,
            inrate: 0.5

        }, {
            time: "2017/03/04 22:22:40",
            size: 100,
            insize: 100,
            inrate: 0.5

        }, {
            time: "2017/03/04 22:22:43",
            size: 100,
            insize: 100,
            inrate: 0.5

        }, {
            time: "2017/03/04 22:22:46",
            size: 100,
            insize: 100,
            inrate: 0.5

        },
        {
            time: "2017/03/04 22:22:49",
            size: 100,
            insize: 100,
            inrate: 0.5

        }];

    function jsonFirst(json) {
        for (var i = 0; i < 10; i++) {
            keliudata.push({
                name: json[i].time,
                value: [json[i].time,
                    json[i].size
                ]
            });
            visitordata.push({
                name: json[i].time,
                value: [json[i].time,
                    json[i].insize
                ]
            });
            visperdata.push({
                name: json[i].time,
                value: [json[i].time,
                    json[i].inrate
                ]
            });

        }
    }

    jsonFirst(json1);

    var option1 = {

        title: {
            text: '客流量',
            link: "http://127.0.0.1:8080/Bloodborne/traffic",
            target: 'self'
        },
        tooltip: {
            trigger: 'axis',
            formatter: function (params) {
                params = params[0];
                // var date = new Date(params.name);
                // return date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear() + ' : ' + params.value[1];
                return params.name + ' ' + params.value[1];
            },
            axisPointer: {
                animation: false
            }
        },
        visualMap: {
            show: false,
            type: 'continuous',
            seriesIndex: 0,
            min: 0,
            max: 30
        },
        xAxis: {
            type: 'time',
            splitLine: {
                show: false
            }
        },
        yAxis: {
            type: 'value',
            boundaryGap: [0, '100%'],
            splitLine: {
                show: false
            }
        },
        series: [{
            name: '客流量模拟数据',
            smooth: 'true',
            areaStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 0,
                        color: 'rgba(199, 237, 250,1)'
                    }, {
                        offset: 1,
                        color: 'rgba(199, 237, 250,0.1)'
                    }], false)
                }
            },
            type: 'line',
            showSymbol: false,
            hoverAnimation: false,
            data: keliudata
        }]

    };
    keliucharts.setOption(option1);

// setInterval(function () {
//
//     for (var i = 0; i < 1; i++) {
//         keliudata.shift();
//         keliudata.push(randomData());
//     }
//
//     keliucharts.setOption({
//         series: [{
//             data: keliudata
//         }]
//     });
//
//
// }, 3000);
// window.onresize=keliucharts.resize;
    window.addEventListener("resize", function () {

        keliucharts.resize();

    });


// var visitordata = [];
// var now = +new Date();
// var threeSec = 3 * 1000;
// var value = Math.random() * 1000;
// for (var i = 0; i < 50; i++) {
//     visitordata.push(randomData());
// }
//
// function randomData() {
//     now = new Date(+now + threeSec);
//     value = value + Math.random() * 21 - 10;
//     return {
//         name: now.toString(),
//         value: [
//             ([now.getFullYear(), now.getMonth() + 1, now.getDate()].join('/') + ' ' + now.getHours() + ':' + now.getMinutes() + ':' + now.getSeconds()),
//             Math.round(value)
//         ]
//     }
// }

    var option2 = {
        title: {
            text: '入店量',
            link: "http://127.0.0.1:8080/Bloodborne/traffic",
            target: 'self'
        },
        visualMap: {
            show: false,
            type: 'continuous',
            seriesIndex: 0,
            min: 0,
            max: 30
        },
        tooltip: {
            trigger: 'axis',
            formatter: function (params) {
                params = params[0];
                // var date = new Date(params.name);
                return params.name + ' ' + params.value[1];
            },
            axisPointer: {
                animation: false
            }
        },
        xAxis: {
            type: 'time',
            splitLine: {
                show: false
            }
        },
        yAxis: {
            type: 'value',
            boundaryGap: [0, '100%'],
            splitLine: {
                show: false
            }
        },
        series: [{
            name: '入店量模拟数据',
            smooth: 'true',
            type: 'line',
            areaStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 0,
                        color: 'rgba(199, 237, 250,1)'
                    }, {
                        offset: 1,
                        color: 'rgba(199, 237, 250,0.1)'
                    }], false)
                }
            },
            showSymbol: false,
            hoverAnimation: false,
            data: visitordata
        }]

    };
    visitorcharts.setOption(option2);

// setInterval(function () {
//
//     for (var i = 0; i < 1; i++) {
//         visitordata.shift();
//         visitordata.push(randomData());
//     }
//
//     visitorcharts.setOption({
//         series: [{
//             data: visitordata
//         }]
//     });
// }, 3000);
// window.onresize=visitorcharts.resize;
    window.addEventListener("resize", function () {

        visitorcharts.resize();

    });


// var visperdata = [0.01, 0.02];
// var now = +new Date();
// var threeSec = 3 * 1000;
// var value = Math.random();
// for (var i = 0; i < 400; i++) {
//     visperdata.push(randomData());
// }
// function randomData() {
//     now = new Date(+now + threeSec);
//     if (value > 1)
//         value = (value - Math.random() / 5);
//     else if (value < 0)
//         value = (value + Math.random() / 5);
//     else
//         value = (value + Math.random() / 5 - 0.1);
//     return {
//         name: now.toString(),
//         value: [
//             ([now.getFullYear(), now.getMonth() + 1, now.getDate()].join('/') + ' ' + now.getHours() + ':' + now.getMinutes() + ':' + now.getSeconds()),
//             (value.toFixed(4))
//         ]
//     }
// }


    var option3 = {
        title: {
            text: '入店率',
            link: "http://127.0.0.1:8080/Bloodborne/traffic",
            target: 'self'
        },
        visualMap: {
            show: false,
            type: 'continuous',
            seriesIndex: 0,
            min: 0,
            max: 1
        },
        tooltip: {
            trigger: 'axis',
            formatter: function (params) {
                params = params[0];
                // var date = new Date(params.name);
                return params.name + ' ' + params.value[1];
            },
            axisPointer: {
                animation: false
            }
        },
        xAxis: {
            type: 'time',
            splitLine: {
                show: false
            }
        },
        yAxis: {
            type: 'value',
            boundaryGap: [0, '100%'],
            splitLine: {
                show: false
            }
        },
        series: [{
            name: '入店率模拟数据',
            type: 'line',
            showSymbol: false,
            smooth: 'true',
            areaStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 0,
                        color: 'rgba(199, 237, 250,1)'
                    }, {
                        offset: 1,
                        color: 'rgba(199, 237, 250,0.1)'
                    }], false)
                }
            },
            hoverAnimation: false,
            data: visperdata
        }]

    };
    vispercharts.setOption(option3);

// setInterval(function () {
//
//     for (var i = 0; i < 1; i++) {
//         visperdata.shift();
//         visperdata.push(randomData());
//     }
//
//     vispercharts.setOption({
//         series: [{
//             data: visperdata
//         }]
//     });
// }, 3000);
// window.onresize=vispercharts.resize;
    window.addEventListener("resize", function () {

        vispercharts.resize();

    });


    var option4 = {
        color: ['#c23531'],
        //#3398DB
        title: {
            text: '来访周期分布',
            link: "http://127.0.0.1:8080/Bloodborne/period",
            target: 'self'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: [
            {
                type: 'category',
                data: ['≤1 day', '≤1 week', '≤1 month', '≤1 season', '≤1/2 year', '≤1 year'],
                axisTick: {
                    alignWithLabel: true
                }
                // x轴需要全部显示时添加
                // axisLabel:{
                //     interval:0
                // }
            }
        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: [
            {
                name: '顾客人数',
                type: 'bar',
                barWidth: '60%',
                data: [10, 52, 200, 334, 390, 330],
                itemStyle: {
                    normal: {

                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: 'rgba(194, 53,49, 1)'
                        }, {
                            offset: 1,
                            color: 'rgba(194, 53,49, 0.3)'
                        }]),
                        shadowColor: 'rgba(0, 0, 0, 0.3)',
                        shadowBlur: 10

                    }
                }
            }
        ]
    };

    vlvcharts.setOption(option4);
    window.addEventListener("resize", function () {

        vlvcharts.resize();

    });


    var option5 = {
        color: ['#c23531'],
        //#3398DB
        title: {
            text: '驻店时长分布',
            link: "http://127.0.0.1:8080/Bloodborne/intime",
            target: 'self'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: [
            {
                type: 'category',
//                data: ['≤1min', '1~3min', '3~5min', '5~10min', '>10min'],
                data: ['≤10s', '10s~30s', '30s~1min', '1~2min', '>2min'],
                axisTick: {
                    alignWithLabel: true
                },
                // x轴需要全部显示时添加
                axisLabel: {
                    interval: 0
                }
            }
        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: [
            {
                name: '人数',
                type: 'bar',
                barWidth: '60%',
                data: [10, 100, 200, 400, 334],
                itemStyle: {
                    normal: {

                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: 'rgba(194, 53,49, 1)'
                        }, {
                            offset: 1,
                            color: 'rgba(194, 53,49, 0.3)'
                        }]),
                        shadowColor: 'rgba(0, 0, 0, 0.3)',
                        shadowBlur: 10

                    }
                }
            }
        ]
    };

    tscharts.setOption(option5);
    window.addEventListener("resize", function () {

        tscharts.resize();

    });


    var option7 = {

        title: {
            text: '顾客活跃度',
            link: "http://127.0.0.1:8080/Bloodborne/period",
            target: 'self'
            // left: 'center',
            // top: 20,
            // textStyle: {
            //     color: '#ccc'
            // }
        },
        // legend: {
        //     orient: 'vertical',
        //     left: 'right',
        //     data: ['高活跃度','中活跃度','低活跃度','沉睡客户']
        // },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },

        series: [
            {
                color: ['#c23531', '#61a0a8', '#d48265', '#91c7ae'],
                name: '顾客类型',
                type: 'pie',
                radius: ['20%', '55%'],
                center: ['50%', '50%'],
                data: [
                    {value: 300, name: '高活跃度'},
                    {value: 400, name: '中活跃度'},
                    {value: 150, name: '低活跃度'},
                    {value: 200, name: '沉睡客户'}
                ],
                label: {
                    normal: {
                        textStyle: {}
                    }
                },
                labelLine: {
                    normal: {
                        lineStyle: {},
                        smooth: 0.2,
                        length: 10,
                        length2: 20
                    }
                },
                // itemStyle: {
                //     normal: {
                //         color: '#c23531',
                //     }
                // },
                itemStyle: {
                    normal: {},
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }


            }
        ]
    };
    vacharts.setOption(option7);
    window.addEventListener("resize", function () {

        vacharts.resize();

    });


    var option8 = {
        color: ['#c23531', '#91c7ae'],
        title: {
            text: "新老顾客占比",
            link: "http://127.0.0.1:8080/Bloodborne/type",
            target: 'self'
        },
        grid: {
            top: 0
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        // legend: {
        //     orient: 'vertical',
        //     left: 'right',
        //     data: ['新顾客','老顾客']
        // },
        series: [
            {
                name: '顾客类别',
                type: 'pie',
                // radius : '55%',
                radius: ['20%', '55%'],
                center: ['50%', '50%'],
                data: [
                    {value: 200, name: '新顾客'},
                    {value: 500, name: '老顾客'}
                ],
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    nocharts.setOption(option8);
    window.addEventListener("resize", function () {

        nocharts.resize();

    });


    var option9 = {
        color: ['#c23531', '#91c7ae', '#d48265'],
        title: {
            text: "深访/跳出占比",
            link: "http://127.0.0.1:8080/Bloodborne/deep",
            target: 'self'
        },
        tooltip: {
            trigger: 'item',
            formatter: "{b} : {c} ({d}%)"
        },
        // grid:{
        //     height:'70%'
        // },
        // legend: {
        //     orient: 'vertical',
        //     left: 'right',
        //     data: ['深访率','跳出率','正常客流占比']
        // },
        series: [
            {
                name: '顾客类别',
                type: 'pie',
                radius: ['20%', '55%'],
                center: ['50%', '50%'],
                data: [
                    {value: 0.2, name: '深访率'},
                    {value: 0.3, name: '跳出率'},
                    {value: 0.5, name: '正常客流占比'}

                ],
                roseType: 'area',
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    jicharts.setOption(option9);
    window.addEventListener("resize", function () {

        jicharts.resize();

    });
});

