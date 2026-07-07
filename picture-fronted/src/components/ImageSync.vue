<template>
  <div class="ai-picture-generate">
    <a-input-group compact style="margin-bottom: 16px">
      <a-textarea
        v-model:value="prompt"
        :rows="3"
        style="width: calc(100% - 120px)"
        placeholder="请输入提示词，描述你想生成的图片"
      />
      <a-button type="primary" :loading="loading" @click="doGenePicture" style="width: 120px">
        生成
      </a-button>
    </a-input-group>

    <img
      v-if="picture?.url"
      :src="picture?.url"
      alt="generated_picture"
      style="max-width: 100%; max-height: 400px; margin-bottom: 16px; border-radius: 8px"
    />
  </div>
</template>

<script setup lang="ts">
import {
  createTexttoImageTaskUsingPost,
  getTexttoImageTaskUsingGet,
  uploadPictureByUrlUsingPost,
} from '@/api/pictureController'
import { message } from 'ant-design-vue'
import { ref } from 'vue'

const loading = ref<boolean>(false)
const prompt = ref<string>('')
const pictureUrl = ref<string>('')

interface Props {
  picture?: API.PictureVO
  spaceId?: number
  onSuccess?: (newPicture: API.PictureVO) => void
}

const props = defineProps<Props>()

const buildRequest = (promptText: string): API.CreateTexttoImageTaskRequest => {
  return {
    input: {
      prompt: promptText,
    },
    parameters: {
      size: '2048*2048',
    },
  }
}

/**
 * 调用 AI 生成图片
 */
const handleGenerate = async (): Promise<string | null> => {
  const promptText = prompt.value?.trim()
  if (!promptText) {
    message.warning('请输入提示词')
    return null
  }
  console.log('promptText:', JSON.stringify(promptText))
  loading.value = true
  try {
    const res = await createTexttoImageTaskUsingPost({}, buildRequest(promptText))
    if (res.data.code === 0 && res.data.data) {
      message.success('创建任务成功，请耐心等待，不要退出界面')

      const imageUrl = await startPolling(res.data.data)
      return imageUrl
    } else {
      message.error('图片生成失败：' + res.data.message)
      return null
    }
  } finally {
    loading.value = false
  }
}

const startPolling = (localTaskId: string): Promise<string | null> => {
  return new Promise((resolve) => {
    let count = 0
    const maxCount = 60 // 最多轮询2分钟

    const timer = setInterval(async () => {
      count++

      if (count > maxCount) {
        clearInterval(timer)
        message.error('任务超时')
        resolve(null)
        return
      }

      try {
        const res = await getTexttoImageTaskUsingGet({
          localTaskId: localTaskId,
        })

        if (res.data.code !== 0 || !res.data.data) {
          return // 继续轮询
        }

        const task = res.data.data
        if (task.status === 'SUCCEEDED') {
          clearInterval(timer)
          resolve(task.result) // 返回图片 URL
        } else if (task.status === 'FAILED') {
          clearInterval(timer)
          message.error('失败：' + (task.error || '未知错误'))
          resolve(null)
        }
        // 其他状态继续轮询
      } catch (e) {
        console.error('轮询异常', e)
      }
    }, 2000)
  })
}

const imageCropperRef = ref()

/**
 * 点击生成：获取图片 -> 预览 -> 打开裁剪弹窗
 */
const doGenePicture = async () => {
  const pictureurl = await handleGenerate()
  if (!pictureurl) {
    return
  }
  pictureUrl.value = pictureurl
  message.success('图片生成成功')
  const params: API.PictureUploadRequest = {
    fileUrl: pictureurl,
    spaceId: props.spaceId,
  }
  const res = await uploadPictureByUrlUsingPost(params)
  if (res.data.code === 0 && res.data.data) {
    message.success('图片上传成功')
    console.log('上传返回:', JSON.stringify(res.data.data))
    props.onSuccess?.(res.data.data)
  } else {
    message.error('图片上传失败，' + res.data.message)
  }
}
</script>
