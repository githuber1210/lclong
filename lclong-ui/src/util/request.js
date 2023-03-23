import axios from 'axios'
import router from "@/router";
import {serverIp} from "../../public/config";
import ElementUI from "element-ui";
const request = axios.create({
    baseURL: 'http://' + serverIp + ':9999',
    timeout: 30000
})

request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    let token = localStorage.getItem("token") ? JSON.parse(localStorage.getItem("token")) : null
    if (token) {
        config.headers['Authorization'] = token;
    }
    return config
}, error => {
    return Promise.reject(error)
});

request.interceptors.response.use(
    response => {
        let res = response.data;
        // 如果是返回的文件
        if (response.config.responseType === 'blob') {
            return res
        }
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        // 当权限验证不通过的时候给出提示
        if (res.code === 401) {
            ElementUI.Message({
                message: res.msg,
                type: 'error'
            });
        }
        return res;
    },
    error => {
        console.log('err' + error) // for debug
        return Promise.reject(error)
    }
)


export default request

