Array.prototype.unique=function(){
    //必须先进行排序
    this.sort();
    var newArr=new Array();
    //放入第一个数组头数据。
    var temp=this[0];
    newArr.push(temp);
    for(var i=1;i<this.length;i++){
        if(temp!=this[i]){ //判断数组中，是否拥有该值。
            temp=this[i]; //放置到数组里面。
            newArr.push(this[i]);
        }
    }
    return newArr;
}
/**
 *
 * @param another
 * @returns 两个数组求并集
 */
Array.prototype.union=function(another){
    return this.concat(another).unique(); //连接之后去重
}
/**
 *
 * @param another
 * @returns 两个数组求交集
 */
Array.prototype.intersect=function(another){
    var newArr=[];

    if(another==[]||another.length==0){ //如果传入进来的是空数组，就返回原数组。
        newArr=this.concat();
        return newArr;
    }
    //为了避免对原数组造成破坏，这里新创建一个数组。
    var clone=this.concat();
    clone.sort(); //排序
    for(var i=0,len=clone.length;i<len;i++){
        var num=clone[i];
        var flag=true;
        for(var j=0,length=another.length;j<length;j++){
            if(num==another[j]){
                flag=false;
                break; //相同，就结束循环。
            }
        }
        if(!flag){ //一样的话，才放置。
            newArr.push(num);
        }
    }
    return newArr;
}
/**
 *
 * @param another
 * @returns 两个数组求差集，保留调用者数组里面的值
 */
Array.prototype.minus=function(another){
    var newArr=[];

    if(another==[]||another.length==0){ //如果传入进来的是空数组，就返回原数组。
        newArr=this.concat();
        return newArr;
    }
    //为了避免对原数组造成破坏，这里新创建一个数组。
    var clone=this.concat();
    clone.sort();
    for(var i=0,len=clone.length;i<len;i++){
        var num=clone[i];
        var flag=true;
        for(var j=0,length=another.length;j<length;j++){
            if(num==another[j]){
                flag=false;
                break; //相同，就结束循环。
            }
        }
        if(flag){ //不一样
            newArr.push(num);
        }
    }
    return newArr;
}
/**
 *
 * @param another
 * @returns 两个数组求补集，返回 another 数组中特有的元素。
 */
Array.prototype.complement=function(another){
    //按照数学上规则，为 :  AUB -A
    var clone=this.concat();
    return clone.union(another).minus(this);
}
