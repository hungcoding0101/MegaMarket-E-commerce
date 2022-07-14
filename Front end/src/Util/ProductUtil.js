
export const productUtil = {
        findProduct: function(id, list){ 
            return list.find(product => product.id === id);
        }
}