<template>
  <div style="color: #666;font-size: 14px;">
    <div style="padding-bottom: 20px">
      <b>{{ user.username }} 先生 / 女士 您好 ，以下为项目介绍</b>
    </div>
    <el-card>
      麻雀
      <el-divider />
      <p>麻雀虽小，五脏俱全</p>
    </el-card>
    <div style="height: 1px; background: #ddd; margin: 20px 0"/>

      <el-col :span="12">
        <div style="padding: 20px 0; font-size: 20px;">技术选型</div>
        <el-row>
          <el-col :span="12" style="line-height: 30px">
            <div><b>后端</b></div>
            <div>SpringBoot</div>
            <div>Mybatis-plus</div>
            <div>JWT</div>
            <div>Redis</div>
          </el-col>
          <el-col :span="12" style="line-height: 30px">
            <b>前端</b>
            <div>Vue2</div>
            <div>ElementUI</div>
          </el-col>
        </el-row>
      </el-col>

    <el-col :span="12">
      <div style="padding: 20px 0; font-size: 20px;">完成功能</div>
      <el-row>
        <el-col :span="12" style="line-height: 30px">
          <div><b>管理模块</b></div>
          <div>用户管理</div>
          <div>角色管理</div>
          <div>菜单管理</div>
          <div>字典管理</div>
          <div>通告管理</div>
          <div>文件管理</div>
        </el-col>
        <el-col :span="12" style="line-height: 30px">
          <div><b>管理模块</b></div>
          <div>操作监控</div>
          <div>用户监控</div>
          <div>数据监控</div>
          <div>服务监控</div>
          <div>缓存监控</div>
          <div>接口监控</div>
        </el-col>
      </el-row>
    </el-col>

  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  }
}
</script>
