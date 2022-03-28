<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
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
          :columns="columns"
          :row-key="record => record.id"
          :data-source="categorys"
          :loading="loading"
          :pagination="false"
      >
        <template #cover="{text:cover}">
          <img v-if="cover" :src="cover" alt="avatar" style="width: 40px; height: 40px;"/>
        </template>
        <template v-slot:action="{text,record}">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              編輯
            </a-button>
            <a-popconfirm
                title="删除后不可恢复，确认删除？"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record.id)"
            >
              <a-button type="danger">
                刪除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal
      title="分类表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="category" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="名称">
        <a-input v-model:value="category.name"/>
      </a-form-item>
      <a-form-item label="父分类">
        <a-select
            ref="select"
            v-model:value="category.parent"
        >
          <a-select-option value="0">
            无
          </a-select-option>
          <a-select-option v-for="c in categorys" :key="c.id" :value="c.id" :disabled="category.id === c.id">
            {{ c.name }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="category.sort"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, ref, toRef} from 'vue';
import axios from 'axios'
import {message} from 'ant-design-vue'
import {Tool} from '@/until/tool'

const listData: Record<string, string>[] = [];


export default defineComponent({
  name: 'AdminCategory',
  setup() {
    const param = ref();
    param.value = {};
    const categorys = ref();
    const level1 = ref();

    const loading = ref(false);

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
        title: '父分类',
        key: 'parent',
        dataIndex: 'parent'
      },
      {
        title: '顺序',
        dataIndex: 'sort'
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
        axios.get("/category/list", {
          params: {
            name: param.value.name
          }
        }).then(
            (resp) => {
              loading.value = false;
              const data = resp.data;
              if (data.success) {
                categorys.value = data.content;
                level1.value = [];
                level1.value = Tool.array2Tree(categorys.value, 0);
                categorys.value = level1.value;
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
      axios.get("/category/all").then(
          (resp) => {
            loading.value = false;
            const data = resp.data;
            if (data.success) {
              categorys.value = data.content;
              level1.value = [];
              level1.value = Tool.array2Tree(categorys.value, 0);
              categorys.value = level1.value;
            } else {
              message.error(data.message);
            }
          }
      )
    }


    /**
     * modal表单
     */
    const category = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;

      axios.post("/category/save", category.value).then(
          (resp) => {
            modalLoading.value = false;
            const data = resp.data;
            if (data.success) {
              modalVisible.value = false;
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
      modalVisible.value = true;
      category.value = Tool.copy(record);
    };

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      category.value = {};
    };

    /**
     * 删除
     */
    const handleDelete = (id: number) => {
      axios.post("/category/delete/" + id).then(
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


    onMounted(() => {
      handleQueryAll();
    });

    return {
      categorys,
      columns,
      loading,
      modalVisible,
      modalLoading,
      category,
      param,
      level1,

      edit,
      add,

      handleModalOk,
      handleDelete,
      handleQuery,
      handleQueryAll,
    };

  }
});
</script>
