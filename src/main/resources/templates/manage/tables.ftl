<@t_admin.head>
<script type="text/javascript">
$(function(){
});
</script>
</@t_admin.head>
<@t_admin.body position="首页" menu="icon4">
<div style="margin: 0 10px;padding: 4px;height: 30px;line-height: 30px;">
	<div style="float: right;">
		<a href="${base}/manage/import-table" class="btn" >导入Excel表格</a>
		<a href="${base}/manage/import-table" class="btn" >从模板选择表格</a>
		<a href="${base}/manage/import-table" class="btn" >手动创建表格</a>
	</div>
</div>

<div style="margin: 0 10px;background: #fff;">
	<table class="tblist">
		<tr class="title">
			<td>表名称</td>
			<td>创建时间</td>
			<td>设置</td>
		</tr>
		
		<#list page.list as item>
		<tr>
			<td style="text-align: left;text-indent: 12px;"><a href="${base}/manage/rows/${item.id}" style="color: #000;">${item.title}</a></td>
			<td>${item.createDate}</td>
			<td>
				<a class="btn" href="${base}/manage/table-set?tableID=${item.id}">表格设置</a>
				<a class="btn" href="${base}/manage/q-set?tableID=${item.id}">查询设置</a>
			</td>
				
		</tr>
		</#list>
	</table>
</div>

</@t_admin.body>