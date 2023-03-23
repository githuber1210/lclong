<template>

  <div class="app-container">
    <el-row>
      <el-col :span="24" class="card-box">
        <el-card>
          <div slot="header"><span>基本信息</span></div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <tbody>
              <tr>
                <td class="el-table__cell is-leaf"><div class="cell">Redis版本</div></td>
                <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.redis_version }}</div></td>
                <td class="el-table__cell is-leaf"><div class="cell">运行模式</div></td>
                <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.redis_mode == "standalone" ? "单机" : "集群" }}</div></td>
                <td class="el-table__cell is-leaf"><div class="cell">端口</div></td>
                <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.tcp_port }}</div></td>
                <td class="el-table__cell is-leaf"><div class="cell">客户端数</div></td>
                <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.connected_clients }}</div></td>
              </tr>
              <tr>
                <td class="el-table__cell is-leaf"><div class="cell">运行时间(天)</div></td>
                <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.uptime_in_days }}</div></td>
                <td class="el-table__cell is-leaf"><div class="cell">使用内存</div></td>
                <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.used_memory_human }}</div></td>
                <td class="el-table__cell is-leaf"><div class="cell">使用CPU</div></td>
                <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ parseFloat(cache.info.used_cpu_user_children).toFixed(2) }}</div></td>
                <td class="el-table__cell is-leaf"><div class="cell">内存配置</div></td>
                <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.maxmemory_human }}</div></td>
              </tr>
              <tr>
                <td class="el-table__cell is-leaf"><div class="cell">AOF是否开启</div></td>
                <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.aof_enabled == "0" ? "否" : "是" }}</div></td>
                <td class="el-table__cell is-leaf"><div class="cell">RDB是否成功</div></td>
                <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.rdb_last_bgsave_status }}</div></td>
                <td class="el-table__cell is-leaf"><div class="cell">Key数量</div></td>
                <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.dbSize">{{ cache.dbSize }} </div></td>
                <td class="el-table__cell is-leaf"><div class="cell">网络入口/出口</div></td>
                <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.instantaneous_input_kbps }}kps/{{cache.info.instantaneous_output_kbps}}kps</div></td>
              </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <el-col :span="8">
        <el-card style="height: calc(100vh - 330px)">
          <div slot="header">
            <span>缓存列表</span>
            <el-button
                style="float: right; padding: 3px 0"
                type="text"
                icon="el-icon-refresh-right"
                @click="refreshCacheNames()"
            ></el-button>
          </div>
          <el-table
              :data="cacheNames"
              :height="tableHeight"
              highlight-current-row
              @row-click="getCacheKeys"
              style="width: 100%"
          >
            <el-table-column
                label="序号"
                width="60"
                type="index"
            ></el-table-column>

            <el-table-column
                label="缓存名称"
                align="center"
                prop="cacheName"
                :show-overflow-tooltip="true"
                :formatter="nameFormatter"
            ></el-table-column>

            <el-table-column
                label="备注"
                align="center"
                prop="remark"
                :show-overflow-tooltip="true"
            />
            <el-table-column
                label="操作"
                width="60"
                align="center"
                class-name="small-padding fixed-width"
            >
              <template slot-scope="scope">
                <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-delete"
                    @click="handleClearCacheName(scope.row)"
                ></el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card style="height: calc(100vh - 330px)">
          <div slot="header">
            <span>键名列表</span>
            <el-button
                style="float: right; padding: 3px 0"
                type="text"
                icon="el-icon-refresh-right"
                @click="refreshCacheKeys()"
            ></el-button>
          </div>
          <el-table
              v-loading="subLoading"
              :data="cacheKeys"
              :height="tableHeight"
              highlight-current-row
              @row-click="handleCacheValue"
              style="width: 100%"
          >
            <el-table-column
                label="序号"
                width="60"
                type="index"
            ></el-table-column>
            <el-table-column
                label="缓存键名"
                align="center"
                :show-overflow-tooltip="true"
                :formatter="keyFormatter"
            >
            </el-table-column>
            <el-table-column
                label="操作"
                width="60"
                align="center"
                class-name="small-padding fixed-width"
            >
              <template slot-scope="scope">
                <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-delete"
                    @click="handleClearCacheKey(scope.row)"
                ></el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card style="height: calc(100vh - 330px)">
          <div slot="header">
            <span>缓存内容</span>
            <el-button
                style="float: right; padding: 3px 0"
                type="text"
                icon="el-icon-refresh-right"
                @click="handleClearCacheAll()"
            >清理全部</el-button
            >
          </div>
          <el-form :model="cacheForm">
            <el-row :gutter="32">
              <el-col :offset="1" :span="22">
                <el-form-item label="缓存名称:" prop="cacheName">
                  <el-input v-model="cacheForm.cacheName" :readOnly="true" />
                </el-form-item>
              </el-col>
              <el-col :offset="1" :span="22">
                <el-form-item label="缓存键名:" prop="cacheKey">
                  <el-input v-model="cacheForm.cacheKey" :readOnly="true" />
                </el-form-item>
              </el-col>
              <el-col :offset="1" :span="22">
                <el-form-item label="缓存内容:" prop="cacheValue">
                  <el-input
                      v-model="cacheForm.cacheValue"
                      type="textarea"
                      :rows="8"
                      :readOnly="true"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>


export default {
  name: "CacheList",
  data() {
    return {
      cacheNames: [],
      cacheKeys: [],
      cacheForm: {},
      loading: true,
      subLoading: false,
      nowCacheName: "",
      tableHeight: window.innerHeight - 200,
      cache: [],
    };
  },
  created() {
    this.getList();
    this.getCacheNames();
  },
  methods: {
    /** 查询缓存名称列表 */
    getCacheNames() {
      this.loading = true;
      this.request.get("/cache/getNames").then(res => {
        if(res.code == 200){
          this.cacheNames = res.data;
          this.loading = false;
        }
      });
    },
    getList() {
      this.request.get("/cache").then(res => {
        if(res.code == 200){
          this.cache = res.data;
        }
      });
    },
    /** 刷新缓存名称列表 */
    refreshCacheNames() {
      this.this.request.get("/cache/getNames")();
      this.$message.success("刷新缓存列表成功");
    },
    /** 清理指定名称缓存 */
    handleClearCacheName(row) {
      this.request.get("/cache/getNames")(row.cacheName).then(res => {
        if(res.code == 200){
          this.$message.success("清理缓存名称[" + this.nowCacheName + "]成功");
          this.getCacheKeys();
        }
      });
    },
    /** 查询缓存键名列表 */
    getCacheKeys(row) {
      const cacheName = row !== undefined ? row.cacheName : this.nowCacheName;
      if (cacheName === "") {
        return;
      }
      this.request.get("/cache/getKeys/" + cacheName).then(res => {
        if(res.code == 200){
          this.cacheKeys = res.data;
          this.nowCacheName = cacheName;
        }
      });
    },
    /** 刷新缓存键名列表 */
    refreshCacheKeys() {
      this.getCacheKeys();
      this.$message.success("刷新键名列表成功");
    },
    /** 清理指定键名缓存 */
    handleClearCacheKey(cacheKey) {
      this.request.delete("/cache/clearCacheName/" + cacheKey).then(res => {
        if (res.code == 200){
          this.$message.success("清理缓存键名[" + cacheKey + "]成功");
          this.getCacheKeys();
        }
      });
    },
    /** 列表前缀去除 */
    nameFormatter(row) {
      return row.cacheName.replace(":", "");
    },
    /** 键名前缀去除 */
    keyFormatter(cacheKey) {
      return cacheKey.replace(this.nowCacheName, "");
    },
    /** 查询缓存内容详细 */
    handleCacheValue(cacheKey) {
      this.request.get("/cache/getValue/" + this.nowCacheName + "/" + cacheKey).then(res => {
        if (res.code == 200) {
          this.cacheForm = res.data;
        }
      });
    },
    /** 清理全部缓存 */
    handleClearCacheAll() {
      this.request.get("/cache/clearCacheAll").then(res => {
        if (res.code == 200) {
          this.$message.success("清理全部缓存成功");
        }
      });
    }
  },
};
</script>
