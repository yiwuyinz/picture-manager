export const PIC_REVIEW_STATUS_ENUM = {
  REVIEWING: 0,
  PASS: 1,
  REJECT: 2,
}

export const PIC_REVIEW_STATUS_MAP = {
  0: '待审核',
  1: '通过',
  2: '拒绝',
}

// export const PIC_REVIEW_STATUS_OPTIONS = Object.keys(PIC_REVIEW_STATUS_MAP).map((key) => {
//   return {
//     label: PIC_REVIEW_STATUS_MAP[key],
//     value: key,
//   }
// })

export const PIC_REVIEW_STATUS_OPTIONS = [
  { value: 0, label: '待审核' },
  { value: 1, label: '通过' },
  { value: 2, label: '拒绝' },
]
