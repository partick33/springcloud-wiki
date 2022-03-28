<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          mode="inline"
          v-model:openKeys="openKeys"
          :style="{ height: '100%', borderRight: 0 }"
          @click="handleClick"
      >
        <a-menu-item key="welcome">
          <MailOtulined/>
          <span>欢迎</span>
        </a-menu-item>
        <a-sub-menu
            :@titleClick="titleClick"
            v-for="item in level1"
            :key="item.id"
        >
          <template #icon>
            <AppstoreOutlined/>
          </template>
          <template #title>{{ item.name }}</template>
          <a-menu-item v-for="child in item.children" :key="child.id">
            <template #icon>
              <AppstoreOutlined/>
            </template>
            <span>{{ child.name }}</span>
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div class="welcome" v-show="isWelcome">
        <TheWelcome/>
      </div>
      <a-list v-show="!isWelcome" item-layout="vertical" size="large" :grid="{gutter: 20, column: 3}"
              :data-source="ebooks">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
              <span>
              <component v-bind:is="'TagOutlined'" style="margin-right: 8px"/>
              {{ item.docCount }}
              </span>
              <span>
              <component v-bind:is="'StarOutlined'" style="margin-right: 8px"/>
              {{ item.viewCount }}
              </span>
              <span>
              <component v-bind:is="'LikeOutlined'" style="margin-right: 8px"/>
              {{ item.voteCount }}
              </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <router-link :to="'/doc?ebookId=' + item.id">
                  {{ item.name }}
                </router-link>
              </template>
              <template #avatar>
                <a-avatar :src="item.cover"/>
              </template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>
</template>


<script lang="ts">
import {defineComponent, onMounted, reactive, ref, toRef} from 'vue';
import axios from 'axios'
import {Tool} from "@/until/tool";
import {message} from "ant-design-vue";
import TheWelcome from '@/components/the-welcome.vue'

const listData: Record<string, string>[] = [];


export default defineComponent({
  name: 'Home',
  components:{
    TheWelcome
  },
  setup() {
    const ebooks = ref();
    const level1 = ref();

    const handleQueryEbook = () => {
      axios.get("/ebook/list",
          {
            params: {
              page: 1,
              size: 1000,
              categoryId2: categoryId2
            }
          }
      ).then(
          (rep) => {
            const data = rep.data;
            ebooks.value = data.content.list;
          });

    };

    const handleQueryCategory = () => {
      axios.get("/category/all").then((rep) => {
        const data = rep.data;
        if (data.success) {
          const categorys = data.content;
          console.log("原始数组：", categorys);
          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);
          console.log("树形结构：", level1.value);
        } else {
          message.error(data.message);
        }
      });
    };

    const isWelcome = ref(true);
    let categoryId2 = 0;

    const handleClick = (value: any) => {
      if (value.key === 'welcome') {
        isWelcome.value = true;
      } else {
        categoryId2 = value.key;
        isWelcome.value = false;
        handleQueryEbook();
      }
    }

    onMounted(() => {
          handleQueryCategory();
        }
    );

    return {
      listData,
      ebooks,
      level1,
      isWelcome,

      handleClick
    };

  }
});
</script>

//scoped意思是只在当前这个页面生效
<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>