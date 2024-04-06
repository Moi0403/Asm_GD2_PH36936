const express = require('express');
const multer = require('multer');
const router = express.Router();



module.exports = router;
router.get('/', (req, res)=>{
    res.send('vao api mobile');
})
const COMMON = require('./COMMON');
const uri = COMMON.uri;
const mongoose = require('mongoose');
const productModel = require('./productModel');
const userModel = require('./userModel');

const storage = multer.diskStorage({
    destination: (req, file, cb) => {
      cb(null, 'upload/'); // Thư mục lưu trữ tệp tin
    },
    filename: function (req, file, cb) {
      cb(null, Date.now() + '-' + file.originalname); // Tên tệp tin
    }
  });

const upload = multer({ storage: storage });

router.get('/list', async (req, res)=>{
    await mongoose.connect(uri);

    let products = await productModel.find();
    console.log(products);
    res.send(products);
});
router.post('/add', async (req, res)=>{
  await mongoose.connect(uri);    


  let product = req.body;
  let kq = await productModel.create(product);
  console.log(kq);

  let products = await productModel.find();
  console.log(products);
  res.send(products);

});
router.delete('/xoa/:id', async (req, res)=>{
    try{
        await mongoose.connect(uri);
    
        let id = req.params.id;
        console.log(id);
    
        const result = await productModel.deleteOne({_id: id});

    
        if (result) {
            let products = await productModel.find();
            console.log(products);
            res.send(products);
          } else {
            res.send('Xóa không thành công');
          }
        } catch (error) {
            console.error('Lỗi khi xóa:', error);
            res.send('Lỗi khi xóa');
          }
});
router.put('/update/:id', async (req, res)=>{
    try {
        const id = req.params.id;
        const data = req.body;

        await mongoose.connect(uri);
        console.log('Kết nối DB thành công');

        const result = await productModel.findByIdAndUpdate(id, data);

        if (result) {
            let products = await productModel.find();
            console.log(products);
            res.send(products);
        } else {
            res.send('Không tìm thấy sản phẩm để cập nhật');
        }
    } catch (error) {
        console.error('Lỗi khi cập nhật:', error);
        res.send('Lỗi khi cập nhật');
    }
});


router.get('/list-user', async (req, res)=>{
    await mongoose.connect(uri);
    let users = await userModel.find();
    console.log(users);
    res.send(users);
});

router.post('/dangki', async (req, res)=>{
    await mongoose.connect(uri);    


    let user = req.body;
    let kq = await userModel.create(user);
    console.log(kq);

    let users = await userModel.find();
    console.log(users);
    res.send(users);
    

});

const bcrypt = require('bcryptjs');
router.post('/login', async (req, res) => {
    const { username, pass } = req.body;
  
    try {
      const user = await userModel.findOne({ username, pass });
      if (user) {
        res.status(200).json({ message: 'Đăng nhập thành công' });
      } else {
        res.status(401).json({ message: 'Sai thông tin đăng nhập' });
      }
    } catch (error) {
      console.error(error);
      res.status(500).json({ message: 'Lỗi máy chủ' });
    }
  });

  router.get('/search/:name', async (req, res) => {
    const ten = req.params.name;
    try {
      let products;
  
      if (ten) {
        products = await productModel.find({ name: ten });
      } else {
        products = await productModel.find();
      }

      res.send(products);
    } catch (error) {
      console.error('Error searching products:', error);
      res.status(500).send('Internal Server Error');
    }
  });

  router.get('/sapxep', async (req, res) => {
    const { sort } = req.query;
  
    try {
      let products;
  
      if (sort === 'asc') {
        products = await productModel.find().sort({ price: 1 });
      } else if (sort === 'desc') {
        products = await productModel.find().sort({ price: -1 });
      } else {
  
        products = await productModel.find();
      }
  
      res.json(products);
    } catch (error) {
      res.status(500).json({ message: 'Đã xảy ra lỗi khi lấy danh sách sản phẩm.' });
    }
  });
  
  
  

module.exports = router;