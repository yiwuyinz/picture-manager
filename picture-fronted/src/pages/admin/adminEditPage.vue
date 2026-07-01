<template>
  <a-form layout="inline" :model="searchParams" @finish="doSearch">
    <a-form-item label="账号">
      <a-input v-model:value="searchParams.userAccount" placeholder="输入账号" />
    </a-form-item>
    <a-form-item label="用户名">
      <a-input v-model:value="searchParams.userName" placeholder="输入用户名" />
    </a-form-item>
    <a-form-item>
      <a-button type="primary" html-type="submit">搜索</a-button>
    </a-form-item>
  </a-form>
  <a-table
    :columns="columns"
    :data-source="dataList"
    bordered
    :pagination="pagination"
    @change="doTableChange"
  >
    <template #bodyCell="{ column, text, record }">
      <template v-if="column.dataIndex === 'createTime'">
              {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') }}
            </template>
            <template v-if="column.dataIndex === 'updateTime'">
              {{ dayjs(record.updateTime).format('YYYY-MM-DD HH:mm:ss') }}
            </template>
      <template
        v-if="
          ['userAccount', 'userName', 'userAvatar', 'userProfile', 'userRole'].includes(
            column.dataIndex,
          )
        "
      >
        <div>
          <a-input
            v-if="editableData[record.id]"
            v-model:value="editableData[record.id][column.dataIndex]"
            style="margin: -5px 0"
          />
          <template v-else>
            <template v-if="column.dataIndex === 'userAvatar'">
              <a-image :src="record.userAvatar" :width="60" />
            </template>
            <template v-if="column.dataIndex === 'userRole'">
              <div v-if="record.userRole === 'admin'">
                <a-tag color="green">管理员</a-tag>
              </div>
              <div v-if="record.userRole === 'user'">
                <a-tag color="blue">用户</a-tag>
              </div>
            </template>
            <template
              v-if="
                column.dataIndex === 'userAccount' ||
                column.dataIndex === 'userName' ||
                column.dataIndex === 'userProfile'
              "
            >
              {{ text }}
            </template>
          </template>
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'operation'">
        <div class="editable-row-operations">
          <span v-if="editableData[record.id]">
            <a-typography-link @click="save(record.id)">保存</a-typography-link>
            <a-popconfirm title="取消？" @confirm="cancel(record.id)">
              <a>取消</a>
            </a-popconfirm>
          </span>
          <span v-else>
            <a @click="edit(record.id)">编辑</a>
            <a-popconfirm title="确认删除" @confirm="doDelect(record.id)">
              <a-typography-link type="danger">删除</a-typography-link type="danger">
            </a-popconfirm>
          </span>
        </div>
      </template>
    </template>
  </a-table>
</template>
<script lang="ts" setup>
import { cloneDeep } from 'lodash-es'
import type { UnwrapRef } from 'vue'

import {
  deleteUserUsingPost,
  listUserVoByPageUsingPost,
  updateUserUsingPost,
} from '@/api/userController'
import { SmileOutlined, DownOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { computed, onMounted, reactive, ref } from 'vue'
import dayjs from 'dayjs'
const columns = [
  {
    title: 'id',
    dataIndex: 'id',
  },
  {
    title: '账号',
    dataIndex: 'userAccount',
  },
  {
    title: '用户名',
    dataIndex: 'userName',
  },
  {
    title: '头像',
    dataIndex: 'userAvatar',
  },
  {
    title: '简介',
    dataIndex: 'userProfile',
  },
  {
    title: '用户角色',
    dataIndex: 'userRole',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
  },
  {
    title: '操作',
    key: 'action',
    dataIndex: 'operation',
  },
]

// 数据
const dataList = ref([])
const total = ref(0)

// 搜索条件
const searchParams = reactive<API.UserQueryRequest>({
  current: 1,
  pageSize: 10,
})

// 获取数据
const fetchData = async () => {
  const res = await listUserVoByPageUsingPost({
    ...searchParams,
  })
  if (res.data.data) {
    dataList.value = res.data.data.records ?? []
    total.value = res.data.data.total ?? 0
  } else {
    message.error('获取数据失败，' + res.data.message)
  }
}

//分页参数
const pagination = computed(() => {
  return {
    current: searchParams.current ?? 1,
    pageSize: searchParams.pageSize ?? 10,
    total: total.value,
    showSizeChanger: true,
    showTotal: (total: any) => `共 ${total} 条`,
  }
})
//表格变化处理
const doTableChange = (page: any) => {
  searchParams.current = page.current
  searchParams.pageSize = page.pageSize
  fetchData()
}
//搜索
const doSearch = () => {
  //重置页码
  searchParams.current = 1
  fetchData()
}

// 页面加载时请求一次
onMounted(() => {
  fetchData()
})

//删除数据
const doDelect = async (id: string) => {
  if (!id) {
    return
  }
  const res = await deleteUserUsingPost({ id })
  if (res.data.code === 0) {
    message.success('已删除')
    fetchData()
  } else {
    message.error('删除失败')
  }
}

//编辑数据
const doEidt = async (values: any) => {
  if (!values) {
    return
  }
  const res = await updateUserUsingPost(values)
  if (res.data.code === 0) {
    message.success('更新成功')
    fetchData()
  } else {
    message.error('更新失败' + res.data.message)
  }
}

const editableData: UnwrapRef<Record<string, any>> = reactive({})

const edit = (key: string) => {
  editableData[key] = cloneDeep(dataList.value.filter((item) => key === item.id)[0])
}
const save = (id: string) => {
  Object.assign(dataList.value.filter((item) => id === item.id)[0], editableData[id])
  const updateData = {
    id: id,
    userAccount: editableData[id].userAccount,
    userName: editableData[id].userName,
    userAvatar: editableData[id].userAvatar,
    userProfile: editableData[id].userProfile,
    userRole: editableData[id].userRole,
  }
  doEidt(updateData)
  delete editableData[id]
}
const cancel = (key: string) => {
  delete editableData[key]
}
</script>
<style scoped>
.editable-row-operations a {
  margin-right: 8px;
}
</style>
