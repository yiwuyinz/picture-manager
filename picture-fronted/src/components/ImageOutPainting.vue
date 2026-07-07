<template>
  <a-modal
    class="image-out-painting"
    v-model:open="visible"
    title="AI 扩图"
    :footer="false"
    @cancel="closeModal"
  >
    <a-row gutter="16">
      <a-col span="12">
        <h4>原始图片</h4>
        <img :src="picture?.url" :alt="picture?.name" style="max-width: 100%" />
      </a-col>
      <a-col span="12">
        <h4>扩图结果</h4>
        <img
          v-if="resultImageUrl"
          :src="resultImageUrl"
          :alt="picture?.name"
          style="max-width: 100%"
        />
      </a-col>
    </a-row>
    <div style="margin-bottom: 16px" />
    <a-flex gap="16" justify="center">
      <a-button type="primary" :loading="!!taskId" ghost @click="createTask"> 生成图片 </a-button>
      <a-button type="primary" v-if="resultImageUrl" :loading="uploadLoading" @click="handleUpload">
        应用结果
      </a-button>
    </a-flex>
  </a-modal>
</template>

<script setup lang="ts">
import { onUnmounted, ref } from 'vue'
import {
  createPictureOutPaintingTaskUsingPost,
  getPictureOutPaintingTaskUsingGet,
  uploadPictureByUrlUsingPost,
  uploadPictureUsingPost,
} from '@/api/pictureController'
import { message } from 'ant-design-vue'

interface Props {
  picture?: API.PictureVO
  spaceId?: number
  onSuccess?: (newPicture: API.PictureVO) => void
}
const resultImageUrl = ref<string>()

const props = defineProps<Props>()

// 是否可见
const visible = ref(false)

// 打开弹窗
const openModal = () => {
  visible.value = true
}

// 关闭弹窗
const closeModal = () => {
  visible.value = false
}

// 暴露函数给父组件
defineExpose({
  openModal,
})

// 任务 id
let taskId = ref<string>()

/**
 * 创建任务
 */
const createTask = async () => {
  if (!props.picture?.id) {
    return
  }
  const res = await createPictureOutPaintingTaskUsingPost(
    {},
    {
      pictureId: props.picture.id,
      // 可以根据需要设置扩图参数
      parameters: {
        xScale: 2,
        yScale: 2,
      },
    },
  )
  if (res.data.code === 0 && res.data.data) {
    message.success('创建任务成功，请耐心等待，不要退出界面')
    console.log(res.data.data)
    taskId.value = res.data.data
    startPolling(taskId.value)
  } else {
    message.error('创建任务失败，' + res.data.message)
  }
}

const startPolling = (localTaskId: string) => {
  const timer = setInterval(async () => {
    const res = await getPictureOutPaintingTaskUsingGet({
      localTaskId: localTaskId,
    })

    const task = res.data.data

    if (task.status === 'SUCCEEDED') {
      clearInterval(timer)
      resultImageUrl.value = task.result
      message.success('扩图完成')
    } else if (task.status === 'FAILED') {
      clearInterval(timer)
      message.error('失败：' + task.error)
    }
    // QUEUED/SUBMITTED/RUNNING 继续轮询
  }, 2000)
}

const uploadLoading = ref<boolean>(false)

const handleUpload = async () => {
  uploadLoading.value = true
  try {
    const params: API.PictureUploadRequest = {
      fileUrl: resultImageUrl.value,
      spaceId: props.spaceId,
    }
    if (props.picture) {
      params.id = props.picture.id
    }
    const res = await uploadPictureByUrlUsingPost(params)
    if (res.data.code === 0 && res.data.data) {
      message.success('图片上传成功')
      // 将上传成功的图片信息传递给父组件
      props.onSuccess?.(res.data.data)
      // 关闭弹窗
      closeModal()
    } else {
      message.error('图片上传失败，' + res.data.message)
    }
  } catch (error) {
    message.error('图片上传失败')
  } finally {
    uploadLoading.value = false
  }
}
</script>

<style scoped>
.image-out-painting {
  text-align: center;
}
</style>
