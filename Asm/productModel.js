const mongoose = require('mongoose');

const ProductSchema = new mongoose.Schema({
    name: {
        type: String,
        require: true
    },
    price: {
        type: Number,
        require: true
    },
    img:{
        type: String,
        require: true
    }, 
    ploai: {
        type: String
    }
});

const ProductModel = new mongoose.model('product', ProductSchema);

module.exports = ProductModel;