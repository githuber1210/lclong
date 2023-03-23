<template>
  <i-frame :src="url" />
</template>

<script>
import iFrame from "@/components/iFrame/index.vue";
import {serverIp} from "../../../../public/config";
export default {
  name: "Druid",
  components: { iFrame },
  data() {
    return {

      url: "http://" + serverIp + ":9999/druid/index.html"
    };
  },
};
</script>
