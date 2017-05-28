//存放页面主要交互逻辑
//js代码模块化

var secklill = {
    //封装秒杀相关的ajax的URL
    URL:{
        now:function () {
            return '/seckill/time/now';
        },
        
        exposer:function (seckillId) {
            return  '/seckill/' + seckillId + '/exposer';
        },
        execute:function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execute';
        }
    },

    validatePhone:function(phone){
        if(phone && phone.length == 11 && !isNaN(phone)){
            return true;
        }else{
            return false;
        }
    },

    handleSecondkill:function (seckillId, node) {
        //获取秒杀地址， 控制显示逻辑， 执行秒杀
        node.hide()
            .html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>')
        $.post(secklill.URL.exposer(seckillId), {}, function(result){
            //在回调函数中执行交互流程
            if(result && result['success']){
                var exposer = result['data'];
                if(exposer['exposed']){
                    //开启秒杀
                    //获取秒杀地址
                    var md5 = exposer['md5'];
                    var killUrl = secklill.URL.execute(seckillId, md5);
                    //绑定一次点击事件
                    $('#killBtn').one('click', function () {
                        //执行秒杀请求
                        //1.先禁用按钮
                        $(this).addClass('disabled');
                        //2.发送秒杀请求
                        $.post(killUrl, {}, function (result) {
                            if(result && result['success']){
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //3.显示秒杀结果
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                        });
                    });
                    node.show();
                } else {
                    //未开启
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    //用户计时系统可能不一致，
                    //重新计算计时逻辑
                    secklill.countDown(seckillId, now, start, end);
                }
            }else{
                console.log('result : ' + result);
            }
        });
    },

    countDown:function (seckillId, nowTime, startTime, endTime) {
        var box = $('#seckill-box');
        //秒杀结束
        if(nowTime > endTime){
            box.html('秒杀活动已结束!');
        }else if(nowTime < startTime){
            var killTime = new Date(startTime + 1000);
            box.countdown(killTime, function(event){
                //时间格式
                var format = event.strftime('秒杀倒计时 ： %D天 %H时 %M分 %S秒');
                box.html(format);
            }).on('finish.countdown', function(){
                //倒计时完成后，执行回调事件
                //获取秒杀地址， 控制显示逻辑， 执行秒杀
                secklill.handleSecondkill(seckillId, box);
            });
            // box.html('秒杀未开始!');
        }else{
            //秒杀开始
            secklill.handleSecondkill(seckillId, box);
        }

    },

    //详情页秒杀逻辑
    detail:{
        //页面初始化
        init:function(params){
            //在cookie中查找手机号
            var killPhone = $.cookie('killPhone');
            //验证手机号
            if(!secklill.validatePhone(killPhone)) {
                //没有查找到手机号
                //弹出手机号输入框
                var phoneModal = $('#killPhoneModal');
                phoneModal.modal({
                    show: true,                //显示弹窗
                    backdrop: false,
                    keyboard: false
                });

                $('#killPhoneBtn').click(function(){
                    var inputPhone = $('#killPhoneKey').val();
                    console.log('inputPhone : ' + inputPhone);
                    if (secklill.validatePhone(inputPhone)) {
                        //输入电话号码验证通过
                        //写入cookie
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});
                        //刷新页面
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide()
                            .html('<label class="label-danger">手机号错误!</label>').show(300);
                    }
                });
            }

            //已经登陆
            //计时交互（秒杀倒计时）
            var seckillId = params['seckillId'];
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            $.get(secklill.URL.now(), {}, function(result){
                if(result && result['success']){
                    var nowTime = result['data'];
                    //秒杀时间判断
                    secklill.countDown(seckillId, nowTime, startTime, endTime);
                }else{
                    console.log('result : ' + result);
                }
            });

        }

    }

}
