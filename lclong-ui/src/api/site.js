import request from '../util/request'

// 获得站点配置信息
export function getSiteConfigInfo() {
  return request({
    url: '/site/getById',
    method: 'get'
  })
}

export function updateSiteConfig(data) {
  return request({
    url: '/site/update',
    method: 'post',
    data
  })
}
