import { AutoComplete, Input } from 'antd';
import React, { useState } from 'react'
import { useHistory } from 'react-router-dom';
import { useFetchSuggestions } from '../Hooks/ProductCategoryHooks';

const ProductSearchBar = () => {
    const history = useHistory();
    const [keyWords, setKeyWords] = useState("");
    const suggetions = useFetchSuggestions(keyWords, 0, 10, null, null, true);

    const onSearch = (value) => {
        setKeyWords(value)
    }

    const handleSearch = (value, event) => {
        history.push(
          `/product/list/name?keyWords=${encodeURIComponent(value)}`
        );       
    } 

  return (
    <AutoComplete
      backfill={true}
      filterOption={false}
      options={suggetions.data?.data?.map((category) => ({
        label: category,
        value: category,
      }))}
      style={{ width: "100%" }}
      /*       onSelect={OnSelect} */
      onSearch={onSearch}
    >
   {   <Input.Search
        enterButton
        onSearch={handleSearch}
        placeholder="Bạn muốn mua gì?"
      />}
    </AutoComplete>
  );
}
export default ProductSearchBar;