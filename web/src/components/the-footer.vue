<template>
  <a-layout-footer style="text-align: center">
    patrick电子书@2021
  </a-layout-footer>
</template>

<script lang="ts">
import {defineComponent, onMounted} from 'vue';
import {Tool} from "@/until/tool";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: 'theFooter',
  setup() {
    let websocket: any;
    let token: any;
    const onOpen = () => {
      console.log("WebSocket连接成功,状态码 : ", websocket.readyState)
    };
    const onMessage = (event: any) => {
      console.log("WebSocket收到信息,状态码 : ", event.data);
      notification['info']({
        message: '点赞提醒',
        description: event.data
      });
    };
    const onError = () => {
      console.log("WebSocket连接错误,状态码 : ", websocket.readyState)
    };
    const onClose = () => {
      console.log("WebSocket连接关闭,状态码 : ", websocket.readyState)
    };
    const initWebSocket = () => {
      //连接成功
      websocket.onopen = onOpen;
      //收到消息的回调
      websocket.onmessage = onMessage;
      //连接错误
      websocket.onerror = onError;
      //连接关闭的回调
      websocket.onclose = onClose;
    };

    onMounted(() => {
      //WebSocket
      if ('WebSocket' in window) {
        token = Tool.uuid(10);
        websocket = new WebSocket(process.env.VUE_APP_WS_SERVER + '/ws/' + token);
        initWebSocket();

        //关闭
        //websocket.close();
      } else {
        alert('当前浏览器 不支持')
      }
    });
  }
});
</script>