<template>

  <!--p防止与下面元素重叠 -->
  <p>
    <a-space>
      <a-button type="primary" @click="handlerQuery()" >刷新</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
    </a-space>
  </p>


  <!--乘车人员展示-->
  <a-table :dataSource="passengers.list"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <!--增加编辑乘客按钮    -->

    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
        <a-space>
          <a-popconfirm
              title="删除后不可恢复，确认删除?"
              @confirm="onDelete(record)"
              ok-text="确认" cancel-text="取消">
            <a style="color: red">删除</a>
          </a-popconfirm>
          <a @click="onEdit(record)">编辑</a>
        </a-space>
      </template>
      <!-- 自定义类型使用template   -->
      <template v-else-if="column.dataIndex === 'type'">
        <span v-for="item in PASSENGER_TYPE_ARRAY" :key="item.code">
<!-- record.type为后端传过来的数据   当前常量与后端比较 相同id显示-->
          <span v-if="item.code === record.type">
            {{item.desc}}
          </span>
        </span>
      </template>
    </template>


  </a-table>
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
          <a-select-option v-for="item in PASSENGER_TYPE_ARRAY" :key="item.code" :value="item.code">{{item.desc}}</a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
//reactive数组重新赋值会失去响应式特性
import {defineComponent, ref, onMounted} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: "passenger-view",

  setup() {
    //将复选跟后端一样申请为枚举或常量
    const PASSENGER_TYPE_ARRAY = window.PASSENGER_TYPE;
    let passenger = ref({
      id: undefined,
      memberId: undefined,
      name: undefined,
      idCard: undefined,
      type: undefined,
      createTime: undefined,
      updateTime: undefined,
    });

    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    })

    let loading = ref(false)

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
      {
        title: '操作',
        dataIndex: 'operation'
      }
    ]

    const visible = ref(false);
    const handleOk = () => {
      axios.post("/member/passenger/save", passenger.value
      ).then(response =>{
        let data = response.data;
        if(data.success) {
          notification.success({description:"乘车人基本信息保存成功"})
          visible.value = false;
          //保存成功后进行刷新
          handlerQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
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
          size : pagination.value.pageSize
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
          pagination.value.current = param.page;
          pagination.value.total = data.content.total;
          // 使用reactive只能push
          // passengers.push(...data.content.list)
        }else{
          notification.error({description: data.message});
        }
      })
    }
    const onAdd = ()=>{
      //新增表单同时会同步前端的表单的数据，但不会真的提交，每次点击新增表单时清空passenger
      passenger.value = {};
      visible.value = true;

    }
    const onEdit = (record)=>{
      visible.value = true;
      //避免同步更改响应式数据 通过Tool使得使用不是同一个record
      passenger.value = window.Tool.copy(record)
    }
    const onDelete = (record) => {
      axios.delete("/member/passenger/delete/" + record.id).then((response) => {
        const data = response.data;
        if (data.success) {
          notification.success({description: "删除成功！"});
          handlerQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          notification.error({description: data.message});
        }
      });
    };
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
        size: pagination.value.pageSize
      })
    })

    return {
      PASSENGER_TYPE_ARRAY,
      visible,
      handleOk,
      passenger,
      passengers,
      columns,
      handlerQuery,
      pagination,
      handleTableChange,
      loading,
      onAdd,
      onEdit,
      onDelete
    };
  },
});
</script>
