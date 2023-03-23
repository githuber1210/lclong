import request from '../util/request'

export function getList(params) {
  return request({
    url: '/posts/postPageQuery',
    method: 'get',
    params
  })
}

export function getById(params) {
  return request({
    url: '/posts/getById',
    method: 'get',
    params
  })
}

export function deleteData(params) {
  return request({
    url: '/posts/delete',
    method: 'get',
    params
  })
}

export function createData(data) {
  return request({
    url: '/posts/insert',
    method: 'post',
    data
  })
}

export function updateData(data) {
  return request({
    url: '/posts/update',
    method: 'post',
    data
  })
}

export function getTagList(params) {
  return request({
    url: '/postTag/queryPageable',
    method: 'get',
    params
  })
}

export function mdEditorUploadImage(data) {
  return request({
    url: '/ossController/upload',
    method: 'post',
    data,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

export function setTop(data) {
  return request({
    url: '/posts/setOnTop/' + data,
    method: 'post',
  })
}

export function cancelTop(data) {
  return request({
    url: '/posts/cancelOnTop/' + data,
    method: 'post',
  })
}

export const uploadUrl = 'http://localhost:9999/file/upload'

