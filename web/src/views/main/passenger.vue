<template>

  <!--p防止与下面元素重叠 -->
  <p>
    <a-space>
      <a-button type="primary" @click="handlerQuery()" >刷新</a-button>
      <a-button type="primary" @click="showModal">新增</a-button>
    </a-space>
  </p>


  <!--乘车人员展示-->
  <a-table :dataSource="passengers.list"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading"/>
  <!-- 新增弹窗 -->
  <a-modal v-model:visible="visible" title="乘车人" @ok="handleOk" ok-text="确认" cancel-text="取消">
    <a-form
        :model="passenger"
        name="basic"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 16 }"
        autocomplete="off"
        @finish="onFinish"
        @finishFailed="onFinishFailed"
    >
      <a-form-item label="姓名">
        <a-input v-model:value="passenger.name" />
      </a-form-item>

      <a-form-item label="身份证">
        <a-input v-model:value="passenger.idCard" />
      </a-form-item>

      <a-form-item label="类型">
        <a-select v-model:value="passenger.type">
          <a-select-option value="1">成人</a-select-option>
          <a-select-option value="2">儿童</a-select-option>
          <a-select-option value="3">学生</a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
//reactive数组重新赋值会失去响应式特性
import {defineComponent, ref, reactive, onMounted} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: "passenger-view",

  setup() {

    const passenger = reactive({
      id: undefined,
      memberId: undefined,
      name: undefined,
      idCard: undefined,
      type: undefined,
      createTime: undefined,
      updateTime: undefined,
    });

    const pagination = reactive({
      total: 0,
      current: 1,
      pageSize: 6,
    })

    const loading = ref(false)

    //声明ref可以直接赋值  reactive直接复制响应式失效，变成普通变量，会发生刷新没数据问题
    let passengers = ref({
      list:[]
    });

    const columns = [
      {
        title: '姓名',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: '身份证',
        dataIndex: 'idCard',
        key: 'idCard',
      },
      {
        title: '类型',
        dataIndex: 'type',
        key: 'type',
      },
    ]

    const visible = ref(false);
    const showModal = () => {
      visible.value = true;
    };
    const handleOk = () => {
      axios.post("/member/passenger/save", passenger
      ).then(response =>{
        let data = response.data;
        if(data.success) {
          notification.success({description:"乘车人基本信息保存成功"})
          visible.value = false;
          //保存成功后进行刷新
          handlerQuery({
            page: pagination.current,
            size: pagination.pageSize
          })
        }else {
          notification.error({description: data.message})
        }

      })
    };
    const handlerQuery = param => {
      //刷新默认不传值，向后端发送一次请求，并把页面置为1
      if(!param) {

        param = {
          page: 1,
          size : pagination.pageSize
        }
      }
      //限制刷新次数，通过加载loading
      loading.value = true
      axios.get("/member/passenger/query-list",{
        params:{
          page: param.page,
          size: param.size
        }
      }).then((response) => {
        //无论成功还是失败 不加载动画
        loading.value = false
        let data = response.data;
        if(data.success) {
          passengers.value.list = data.content.list;

          //设置分页控件的值   页码切换问题
          pagination.current = param.page;
          pagination.total = data.content.total;
          // 使用reactive只能push
          // passengers.push(...data.content.list)
        }else{
          notification.error({description: data.message});
        }
      })
    }

    //监听页码事件
    const handleTableChange = (pagination) =>{
      console.log("分页参数"+pagination)
      handlerQuery({
        page: pagination.current,
        size: pagination.pageSize
      })
    }

    onMounted(() => {
      handlerQuery({
        page: 1,
        size: pagination.pageSize
      })
    })

    return {
      visible,
      showModal,
      handleOk,
      passenger,
      passengers,
      columns,
      handlerQuery,
      pagination,
      handleTableChange,
      loading
    };
  },
});
</script>
