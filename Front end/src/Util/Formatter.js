
export const Formatter = {
     addNumberSeparator: function(number){
        return number.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },

    getNumberRawValue: function(number){
        return number.replace(/\$\s?|(,*)/g, "");
    }
}