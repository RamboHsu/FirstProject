<html>
<head>
    <meta charset="utf-8">
    <title>Freemarker 入门小 DEMO </title>
</head>
<body>
<#--我只是一个注释，我不会有任何输出 -->
Hello,${name}...${message}
<#assign linkman="Mr chou"/><br>

<#--define a simple type-->
Contact:${linkman}<br>

<#--define an object type-->
<#assign info={"mobile":"13838384888","address":"CN CQ"}/>
Tel:${info.mobile} Address:${info.address}<br>

<#--'include' instruction-->
<#include "head.ftl"/>

<#--'if' instruction-->
<#if success==true>
    You had pass the Real Name Authentication.<br>
    <#else>
    You had not pass the Real Name Authentication.<br>
</#if>

<#--'list' instruction-->
------------Product Price List-----------<br>
<#list goodsList as goods>
    ${goods_index+1}. name:${goods.name} price:${goods.price};<br>
</#list>
Total items: ${goodsList?size}<br>

<#--json String to Object-->
<#assign text="{'bank':'ICBC','account':'6222023100000000000'}"/>
<#assign data=text?eval/>
Bank:${data.bank}  Account:${data.account}<br>

<#--Date format-->
current date:${today?date}<br>
current time:${today?time}<br>
current date+time:${today?datetime}<br>
date format:${today?string("yyyy-MM-dd")}<br>

<#--Int to String-->
Account Score: ${score}<br>
Account Score(?+c): ${score?c}<br>

<#--null-->
<#if aaa??>
    aaa is existent.<br>
    <#else >
    aaa is non-existent.<br>
</#if>
<#--在代码中不对 aaa 赋值，也不会报错了 ，当 aaa 为 null 则返回！后边的内容--->
${aaa!'-'}




</body>
</html>