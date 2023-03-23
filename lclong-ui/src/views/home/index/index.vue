<template>
  <div>
    <el-carousel height="79vh" :interval="2000">
      <el-carousel-item v-for="item in imgs" :key="item">
        <img :src="item" alt="" style="width: 100%;height: 79vh">
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script>
export default {
  name: 'index',
  data () {
    return {
      imgs: [
        'https://img.zcool.cn/community/01c8f15aeac135a801207fa16836ae.jpg@1280w_1l_2o_100sh.jpg',
          'https://ts1.cn.mm.bing.net/th/id/R-C.15e970cd0765096178a6da16993cfbb1?rik=IT5KfevidZcTig&riu=http%3a%2f%2fimg.pconline.com.cn%2fimages%2fupload%2fupc%2ftx%2fwallpaper%2f1210%2f22%2fc0%2f14558824_1350879506501.jpg&ehk=X9ro%2fg%2fGTmsglVrbV%2bmy8c3wsAvcHseqcEhsf80RMWA%3d&risl=&pid=ImgRaw&r=0',
        'https://ts1.cn.mm.bing.net/th/id/R-C.0dee2228031e4ef5b03d0c5734aef866?rik=BD%2bnjbFbllVmEQ&riu=http%3a%2f%2fimg.zcool.cn%2fcommunity%2f01cf02554336f10000019ae9df1dad.jpg%403000w_1l_2o_100sh.jpg&ehk=zvcYgjHlqK2U2x9ploUbmiBIk7BewUd6lyA0AIswegQ%3d&risl=&pid=ImgRaw&r=0'
      ],

    }
  },
  computed: {
    hoverBackground () {
      return '#ffd04b'
    },
    currentPath () {
      var x = this.$route.path.indexOf('/', 1)
      if (x !== -1) {
        return this.$route.path.substring(0, x)
      } else {
        return this.$route.path
      }
    }
  }
}
</script>

<style scoped>
a{
  text-decoration: none;
}

span {
  pointer-events: none;
}

</style>
