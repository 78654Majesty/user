# WebApi 统一入口
    req
     | 1
build context 封装数据
     | 2
  validate 校验器 <- 3 - 由于是统一的入口 根据类型选择对应的平台类型
     | 4
   adapt 适配器 <- 5 - 
     | 6
  service - 8 -> 返回结果response 
     | 7
    db
