<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-row :gutter="24">
        <a-col :span="8">
          <p>
            <a-form
                layout="inline"
                :model="param"
            >
              <a-form-item>
                <a-input v-model:value="param.name" placeholder="名称">
                </a-input>
              </a-form-item>
              <a-form-item>
                <a-button type="primary" @click="handleQuery">
                  查询
                </a-button>
              </a-form-item>
              <a-button type="primary" @click="add()">
                新增
              </a-button>
            </a-form>
          </p>
          <a-table
              v-if="level1.length > 0"
              :columns="columns"
              :row-key="record => record.id"
              :data-source="level1"
              :loading="loading"
              :pagination="false"
              size="small"
              :defaultExpandAllRows="true"
          >
            <template #name="{text,record}">
              {{ record.sort }} {{ text }}
            </template>
            <template v-slot:action="{text,record}">
              <a-space size="small">
                <a-button type="primary" @click="edit(record)" size="small">
                  編輯
                </a-button>
                <a-popconfirm
                    title="删除后不可恢复，确认删除？"
                    ok-text="是"
                    cancel-text="否"
                    @confirm="handleDelete(record.id)"
                >
                  <a-button type="danger" size="small">
                    刪除
                  </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </a-table>
        </a-col>
        <a-col :span="16">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleSave()">
                  保存
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="doc" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }" layout="vertical">
            <a-form-item>
              <a-input v-model:value="doc.name" placeholder="名称"/>
            </a-form-item>
            <a-form-item>
              <a-tree-select
                  v-model:value="doc.parent"
                  style="width: 100%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  :tree-data="treeSelectData"
                  placeholder="请选择父文档"
                  tree-default-expand-all
                  :replaceFields="{title:'name', key:'id',value:'id'}"
              >
              </a-tree-select>
            </a-form-item>
            <a-form-item>
              <a-input v-model:value="doc.sort" placeholder="顺序"/>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="handlePreviewContent()">
                <EyeOutlined/>
                内容预览
              </a-button>
            </a-form-item>
            <a-form-item>
              <div id="content"></div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>

      <a-drawer width="900" placement="right" :closable="false" :visible="drawerVisible" @close="onDrawerClose">
        <div class="wangeditor" :innerHTML="previewHtml"></div>
      </a-drawer>

    </a-layout-content>
  </a-layout>

  <!--  <a-modal
        title="文档表单"
        v-model:visible="modalVisible"
        :confirm-loading="modalLoading"
        @ok="handleModalOk"
    >

    </a-modal>-->
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, ref, toRef} from 'vue';
import axios from 'axios'
import {message} from 'ant-design-vue'
import {Tool} from '@/until/tool'
import {useRoute} from "vue-router";
import E from "wangeditor"

const listData: Record<string, string>[] = [];


export default defineComponent({
  name: 'AdminDoc',
  setup() {
    const route = useRoute();
    const param = ref();
    param.value = {};
    const docs = ref();
    const level1 = ref();
    level1.value = [];
    const loading = ref(false);
    //因为树形组建的属性状态，会随当前编辑的节点而变化，所以单独声明一个响应式变量
    const treeSelectData = ref();
    treeSelectData.value = [];

    const columns = [
      {
        title: 'id',
        dataIndex: 'id'
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: 'action',
        key: 'action',
        slots: {customRender: 'action'}
      }
    ];

    /**
     * 数据查询
     */
    const handleQuery = (params: any) => {
      if (param.value.name) {
        loading.value = true;
        axios.get("/doc/list", {
          params: {
            name: param.value.name
          }
        }).then(
            (resp) => {
              loading.value = false;
              const data = resp.data;
              if (data.success) {
                docs.value = data.content;
                level1.value = [];
                level1.value = Tool.array2Tree(docs.value, 0);
                docs.value = level1.value;
              } else {
                message.error(data.message);
              }
            }
        );
      } else {
        handleQueryAll();
      }
    }

    /**
     * 数据展示查询
     */
    const handleQueryAll = () => {
      loading.value = true;
      axios.get("/doc/all/" + route.query.ebookId).then(
          (resp) => {
            loading.value = false;
            const data = resp.data;
            if (data.success) {
              docs.value = data.content;
              level1.value = [];
              level1.value = Tool.array2Tree(docs.value, 0);
              docs.value = level1.value;
              treeSelectData.value = Tool.copy(level1.value);
              //为选择树添加一个"无"
              treeSelectData.value.unshift({id: 0, name: '无'});
            } else {
              message.error(data.message);
            }
          }
      )
    }


    /**
     * modal表单
     */
    const doc = ref();
    doc.value = {
      ebookId: route.query.ebookId
    }
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const editor = new E('#content');
    editor.config.zIndex = 0;

    const handleSave = () => {
      modalLoading.value = true;
      doc.value.content = editor.txt.html();
      axios.post("/doc/save", doc.value).then(
          (resp) => {
            modalLoading.value = false;
            const data = resp.data;
            if (data.success) {
              // modalVisible.value = false;
              message.success("保存成功！");
              //重新加载数据
              handleQueryAll()
            } else {
              message.error(data.message);
            }
          }
      )
    };

    /**
     * 编辑
     */
    const edit = (record: any) => {
      //清空富文本框
      editor.txt.html("");
      doc.value = Tool.copy(record);
      handleQueryContent();

      //不能选择当前节点及其所有子孙节点，作为父节点，会使树断开
      treeSelectData.value = Tool.copy(level1.value) || [];
      setDisable(treeSelectData.value, record.id);

      console.log(treeSelectData.value);
      //为选择树添加一个"无"
      treeSelectData.value.unshift({id: 0, name: '无'});
    };

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      doc.value = {
        ebookId: route.query.ebookId
      };

      treeSelectData.value = Tool.copy(level1.value) || [];
      //为选择树添加一个"无"
      treeSelectData.value.unshift({id: 0, name: '无'});
    };

    /**
     * 删除
     */
    const handleDelete = (id: number) => {
      getDeleteIds(level1.value, id);
      axios.post("/doc/delete/" + ids.join(",")).then(
          (resp) => {
            console.log(id)
            const data = resp.data;
            if (data.success) {
              //重新加载数据
              handleQueryAll()
            }
          }
      )
    };

    /**
     * 将某节点及其子孙节点全部部署为disabled
     */
    const setDisable = (treeSelectData: any, id: any) => {
      //遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          //如果当前节点就是目标节点
          console.log("disabled", node);
          //将目标节点设置为disabled
          node.disabled = true;

          //遍历所有字节点，将所有子节点全部都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id);
            }
          }
        } else {
          //如果当前节点不是目标节点，则到其子节点再找找看
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    };

    const ids: Array<string> = [];
    const getDeleteIds = (treeSelectData: any, id: any) => {
      //遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          //如果当前节点就是目标节点
          console.log("deleted", node);
          ids.push(id)

          /**
           * 查找整根树枝
           */
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children, children[j].id);
            }
          }
        } else {
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    };

    /**
     * 内容查询
     */
    const handleQueryContent = () => {
      axios.get("/doc/find-content/" + doc.value.id).then(
          (resp) => {
            const data = resp.data;
            if (data.success) {
              editor.txt.html(data.content);
            } else {
              message.error(data.message);
            }
          }
      )
    }

    const drawerVisible = ref(false);
    const previewHtml = ref();
    const handlePreviewContent = () => {
      const html = editor.txt.html();
      previewHtml.value = html;
      drawerVisible.value = true;
    };
    const onDrawerClose = () => {
      drawerVisible.value = false;
    };

    onMounted(() => {
      handleQueryAll();
      editor.create();
    });

    return {
      docs,
      columns,
      loading,
      modalVisible,
      modalLoading,
      doc,
      param,
      level1,
      treeSelectData,
      drawerVisible,
      previewHtml,

      edit,
      add,

      handleSave,
      handleDelete,
      handleQuery,
      handleQueryAll,
      handlePreviewContent,
      onDrawerClose
    };

  }
});
</script>
