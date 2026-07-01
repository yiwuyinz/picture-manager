<template>
    <a-flex justify="space-between">
  <h2>空间管理</h2>
  <a-space>
    <a-button type="primary" href="/add_space" target="_blank">+ 创建空间</a-button>
  </a-space>
  </a-flex>
<a-form layout="inline" :model="searchParams" @finish="doSearch">
    <a-form-item label="空间名称" name="spaceName">
        <a-input v-model:value="searchParams.spaceName" placeholder="请输入空间名称" allow-clear />
      </a-form-item>
      <a-form-item label="空间级别" name="spaceLevel">
        <a-select v-model:value="searchParams.spaceLevel" :options="SPACE_LEVEL_OPTIONS" placeholder="请输入空间级别" style="min-width: 180px;" allow-clear />
      </a-form-item>
      <a-form-item label="用户id" name="userId">
        <a-input
          v-model:value="searchParams.userId"
          placeholder="请输入用户id"
          allow-clear
        />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">搜索</a-button>
      </a-form-item>
    </a-form>
  
    <a-table
      :columns="columns"
      :data-source="dataList"
      :pagination="pagination"
      @change="doTableChange"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex==='spaceLevel'">
          <a-tag>{{ SPACE_LEVEL_MAP[record.spaceLevel] }}</a-tag>
        </template>
        <template v-if="column.dataIndex==='spaceUseInfo'">
          <a-tag>
            <div>大小：{{ formatSize(record.totalSize) }}/{{ formatSize(record.maxSize) }}</div>
            <div>数量：{{ record.totalCount }}/{{ record.maxCount }}</div>
          </a-tag>
        </template>
        <template v-if="column.dataIndex === 'createTime'">
        {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') }}
      </template>
      <template v-if="column.dataIndex === 'editTime'">
        {{ dayjs(record.editTime).format('YYYY-MM-DD HH:mm:ss') }}
      </template>
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button type="link" :href="`/update_space?id=${record.id}`" target="_blank"
              >编辑</a-button
            >
            <a-popconfirm title="确认删除" @confirm="doDelete(record.id)">
                <a-typography-link type="danger">删除</a-typography-link type="danger">
              </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>
</template>
<script lang="ts" setup>
import { message } from 'ant-design-vue'
import { computed, onMounted, reactive, ref } from 'vue'
import dayjs from 'dayjs'
import { deletePictureUsingPost, listPictureByPageUsingPost } from '@/api/pictureController'
import { SPACE_LEVEL_ENUM, SPACE_LEVEL_MAP, SPACE_LEVEL_OPTIONS } from '@/constants/space'
import { formatSize } from '@/utils'
import { deleteSpaceUsingPost, listSpaceByPageUsingPost } from '@/api/spaceController'
  const columns = [
  {
    title: 'id',
    dataIndex: 'id',
    width: 80,
  },
  {
    title: '空间名称',
    dataIndex: 'spaceName',
  },
  {
    title: '空间级别',
    dataIndex: 'spaceLevel',
  },
  {
    title: '使用情况',
    dataIndex: 'spaceUseInfo',
  },
  {
    title: '用户 id',
    dataIndex: 'userId',
    width: 80,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
  {
    title: '编辑时间',
    dataIndex: 'editTime',
  },
  {
    title: '操作',
    key: 'action',
  },
]

  
  // 数据
  const dataList = ref([])
  const total = ref(0)
  
  // 搜索条件
  const searchParams = reactive<API.SpaceQueryRequest>({
    current: 1,
    pageSize: 10,
    sortField: 'createTime',
    sortOrder: 'descend',
  })
  
  // 获取数据
  const fetchData = async () => {
    const res = await listSpaceByPageUsingPost({
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
  const doDelete = async (id: string) => {
    if (!id) {
      return
    }
    const res = await deleteSpaceUsingPost({ id })
    if (res.data.code === 0) {
      message.success('已删除')
      fetchData()
    } else {
      message.error('删除失败')
    }
  }
  </script>