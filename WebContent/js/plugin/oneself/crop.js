/**
 * 参数说明：
 * @param showId : 裁剪压缩后的图片显示的指定的id标签上
 * @param imgType :
 * 		1、head=头像裁剪
 * 		2、norm=标准规范（主要用作【商品图片】，默认width和height为：800*600）
 * 		3、certification=认证图片（默认width和height为：1200*1200）
 * 		4、zoom=缩放图片（根据指定width宽和height高的最大值进行源图缩放，默认width和height为：800*600）
 * 		5、custom=他自定义宽和高
 * 
 * @param width：根据不同的imgType代表固定宽或最大宽
 * @param height : 同上~~
 * @param isUpload : 是否裁剪后立即上传（true或false【默认】），上传的url为file所属的form
 * @param quality : 压缩质量（默认0.85，1为不压缩【不建议】可能会提高一点所占用的空间）
 * @param imgStyle、imgClass、parentStyle、parentClass：压缩后的图片style、class，以及所包含的父标签的style和class
 * @param imgCallBack : 回调方法名，参数将由crop统一设置（参数：1、image对象；2、处理image后的html）‘’
 * @param max : 一次最多上传多少张（默认不限制）
 */

/* jpeg_encoder_basic.js  for android jpeg压缩质量修复 */
function JPEGEncoder(a){function I(a){var c,i,j,k,l,m,n,o,p,b=[16,11,10,16,24,40,51,61,12,12,14,19,26,58,60,55,14,13,16,24,40,57,69,56,14,17,22,29,51,87,80,62,18,22,37,56,68,109,103,77,24,35,55,64,81,104,113,92,49,64,78,87,103,121,120,101,72,92,95,98,112,100,103,99];for(c=0;64>c;c++)i=d((b[c]*a+50)/100),1>i?i=1:i>255&&(i=255),e[z[c]]=i;for(j=[17,18,24,47,99,99,99,99,18,21,26,66,99,99,99,99,24,26,56,99,99,99,99,99,47,66,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99],k=0;64>k;k++)l=d((j[k]*a+50)/100),1>l?l=1:l>255&&(l=255),f[z[k]]=l;for(m=[1,1.387039845,1.306562965,1.175875602,1,.785694958,.5411961,.275899379],n=0,o=0;8>o;o++)for(p=0;8>p;p++)g[n]=1/(8*e[z[n]]*m[o]*m[p]),h[n]=1/(8*f[z[n]]*m[o]*m[p]),n++}function J(a,b){var f,g,c=0,d=0,e=new Array;for(f=1;16>=f;f++){for(g=1;g<=a[f];g++)e[b[d]]=[],e[b[d]][0]=c,e[b[d]][1]=f,d++,c++;c*=2}return e}function K(){i=J(A,B),j=J(E,F),k=J(C,D),l=J(G,H)}function L(){var c,d,e,a=1,b=2;for(c=1;15>=c;c++){for(d=a;b>d;d++)n[32767+d]=c,m[32767+d]=[],m[32767+d][1]=c,m[32767+d][0]=d;for(e=-(b-1);-a>=e;e++)n[32767+e]=c,m[32767+e]=[],m[32767+e][1]=c,m[32767+e][0]=b-1+e;a<<=1,b<<=1}}function M(){for(var a=0;256>a;a++)x[a]=19595*a,x[a+256>>0]=38470*a,x[a+512>>0]=7471*a+32768,x[a+768>>0]=-11059*a,x[a+1024>>0]=-21709*a,x[a+1280>>0]=32768*a+8421375,x[a+1536>>0]=-27439*a,x[a+1792>>0]=-5329*a}function N(a){for(var b=a[0],c=a[1]-1;c>=0;)b&1<<c&&(r|=1<<s),c--,s--,0>s&&(255==r?(O(255),O(0)):O(r),s=7,r=0)}function O(a){q.push(w[a])}function P(a){O(255&a>>8),O(255&a)}function Q(a,b){var c,d,e,f,g,h,i,j,l,p,q,r,s,t,u,v,w,x,y,z,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,$,_,k=0;const m=8,n=64;for(l=0;m>l;++l)c=a[k],d=a[k+1],e=a[k+2],f=a[k+3],g=a[k+4],h=a[k+5],i=a[k+6],j=a[k+7],p=c+j,q=c-j,r=d+i,s=d-i,t=e+h,u=e-h,v=f+g,w=f-g,x=p+v,y=p-v,z=r+t,A=r-t,a[k]=x+z,a[k+4]=x-z,B=.707106781*(A+y),a[k+2]=y+B,a[k+6]=y-B,x=w+u,z=u+s,A=s+q,C=.382683433*(x-A),D=.5411961*x+C,E=1.306562965*A+C,F=.707106781*z,G=q+F,H=q-F,a[k+5]=H+D,a[k+3]=H-D,a[k+1]=G+E,a[k+7]=G-E,k+=8;for(k=0,l=0;m>l;++l)c=a[k],d=a[k+8],e=a[k+16],f=a[k+24],g=a[k+32],h=a[k+40],i=a[k+48],j=a[k+56],I=c+j,J=c-j,K=d+i,L=d-i,M=e+h,N=e-h,O=f+g,P=f-g,Q=I+O,R=I-O,S=K+M,T=K-M,a[k]=Q+S,a[k+32]=Q-S,U=.707106781*(T+R),a[k+16]=R+U,a[k+48]=R-U,Q=P+N,S=N+L,T=L+J,V=.382683433*(Q-T),W=.5411961*Q+V,X=1.306562965*T+V,Y=.707106781*S,Z=J+Y,$=J-Y,a[k+40]=$+W,a[k+24]=$-W,a[k+8]=Z+X,a[k+56]=Z-X,k++;for(l=0;n>l;++l)_=a[l]*b[l],o[l]=_>0?0|_+.5:0|_-.5;return o}function R(){P(65504),P(16),O(74),O(70),O(73),O(70),O(0),O(1),O(1),O(0),P(1),P(1),O(0),O(0)}function S(a,b){P(65472),P(17),O(8),P(b),P(a),O(3),O(1),O(17),O(0),O(2),O(17),O(1),O(3),O(17),O(1)}function T(){var a,b;for(P(65499),P(132),O(0),a=0;64>a;a++)O(e[a]);for(O(1),b=0;64>b;b++)O(f[b])}function U(){var a,b,c,d,e,f,g,h;for(P(65476),P(418),O(0),a=0;16>a;a++)O(A[a+1]);for(b=0;11>=b;b++)O(B[b]);for(O(16),c=0;16>c;c++)O(C[c+1]);for(d=0;161>=d;d++)O(D[d]);for(O(1),e=0;16>e;e++)O(E[e+1]);for(f=0;11>=f;f++)O(F[f]);for(O(17),g=0;16>g;g++)O(G[g+1]);for(h=0;161>=h;h++)O(H[h])}function V(){P(65498),P(12),O(3),O(1),O(0),O(2),O(17),O(3),O(17),O(0),O(63),O(0)}function W(a,b,c,d,e){var h,l,o,q,r,s,t,u,v,w,f=e[0],g=e[240];const i=16,j=63,k=64;for(l=Q(a,b),o=0;k>o;++o)p[z[o]]=l[o];for(q=p[0]-c,c=p[0],0==q?N(d[0]):(h=32767+q,N(d[n[h]]),N(m[h])),r=63;r>0&&0==p[r];r--);if(0==r)return N(f),c;for(s=1;r>=s;){for(u=s;0==p[s]&&r>=s;++s);if(v=s-u,v>=i){for(t=v>>4,w=1;t>=w;++w)N(g);v=15&v}h=32767+p[s],N(e[(v<<4)+n[h]]),N(m[h]),s++}return r!=j&&N(f),c}function X(){var b,a=String.fromCharCode;for(b=0;256>b;b++)w[b]=a(b)}function Y(a){if(0>=a&&(a=1),a>100&&(a=100),y!=a){var b=0;b=50>a?Math.floor(5e3/a):Math.floor(200-2*a),I(b),y=a,console.log("Quality set to: "+a+"%")}}function Z(){var c,b=(new Date).getTime();a||(a=50),X(),K(),L(),M(),Y(a),c=(new Date).getTime()-b,console.log("Initialization "+c+"ms")}var d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,A,B,C,D,E,F,G,H;Math.round,d=Math.floor,e=new Array(64),f=new Array(64),g=new Array(64),h=new Array(64),m=new Array(65535),n=new Array(65535),o=new Array(64),p=new Array(64),q=[],r=0,s=7,t=new Array(64),u=new Array(64),v=new Array(64),w=new Array(256),x=new Array(2048),z=[0,1,5,6,14,15,27,28,2,4,7,13,16,26,29,42,3,8,12,17,25,30,41,43,9,11,18,24,31,40,44,53,10,19,23,32,39,45,52,54,20,22,33,38,46,51,55,60,21,34,37,47,50,56,59,61,35,36,48,49,57,58,62,63],A=[0,0,1,5,1,1,1,1,1,1,0,0,0,0,0,0,0],B=[0,1,2,3,4,5,6,7,8,9,10,11],C=[0,0,2,1,3,3,2,4,3,5,5,4,4,0,0,1,125],D=[1,2,3,0,4,17,5,18,33,49,65,6,19,81,97,7,34,113,20,50,129,145,161,8,35,66,177,193,21,82,209,240,36,51,98,114,130,9,10,22,23,24,25,26,37,38,39,40,41,42,52,53,54,55,56,57,58,67,68,69,70,71,72,73,74,83,84,85,86,87,88,89,90,99,100,101,102,103,104,105,106,115,116,117,118,119,120,121,122,131,132,133,134,135,136,137,138,146,147,148,149,150,151,152,153,154,162,163,164,165,166,167,168,169,170,178,179,180,181,182,183,184,185,186,194,195,196,197,198,199,200,201,202,210,211,212,213,214,215,216,217,218,225,226,227,228,229,230,231,232,233,234,241,242,243,244,245,246,247,248,249,250],E=[0,0,3,1,1,1,1,1,1,1,1,1,0,0,0,0,0],F=[0,1,2,3,4,5,6,7,8,9,10,11],G=[0,0,2,1,2,4,4,3,4,7,5,4,4,0,1,2,119],H=[0,1,2,3,17,4,5,33,49,6,18,65,81,7,97,113,19,34,50,129,8,20,66,145,161,177,193,9,35,51,82,240,21,98,114,209,10,22,36,52,225,37,241,23,24,25,26,38,39,40,41,42,53,54,55,56,57,58,67,68,69,70,71,72,73,74,83,84,85,86,87,88,89,90,99,100,101,102,103,104,105,106,115,116,117,118,119,120,121,122,130,131,132,133,134,135,136,137,138,146,147,148,149,150,151,152,153,154,162,163,164,165,166,167,168,169,170,178,179,180,181,182,183,184,185,186,194,195,196,197,198,199,200,201,202,210,211,212,213,214,215,216,217,218,226,227,228,229,230,231,232,233,234,242,243,244,245,246,247,248,249,250],this.encode=function(a,b){var d,e,f,m,n,o,p,y,z,A,B,C,D,E,F,G,H,I,J,K,c=(new Date).getTime();for(b&&Y(b),q=new Array,r=0,s=7,P(65496),R(),T(),S(a.width,a.height),U(),V(),d=0,e=0,f=0,r=0,s=7,this.encode.displayName="_encode_",m=a.data,n=a.width,o=a.height,p=4*n,z=0;o>z;){for(y=0;p>y;){for(D=p*z+y,E=D,F=-1,G=0,H=0;64>H;H++)G=H>>3,F=4*(7&H),E=D+G*p+F,z+G>=o&&(E-=p*(z+1+G-o)),y+F>=p&&(E-=y+F-p+4),A=m[E++],B=m[E++],C=m[E++],t[H]=(x[A]+x[B+256>>0]+x[C+512>>0]>>16)-128,u[H]=(x[A+768>>0]+x[B+1024>>0]+x[C+1280>>0]>>16)-128,v[H]=(x[A+1280>>0]+x[B+1536>>0]+x[C+1792>>0]>>16)-128;d=W(t,g,d,i,k),e=W(u,h,e,j,l),f=W(v,h,f,j,l),y+=32}z+=8}return s>=0&&(I=[],I[1]=s+1,I[0]=(1<<s+1)-1,N(I)),P(65497),J="data:image/jpeg;base64,"+btoa(q.join("")),q=[],K=(new Date).getTime()-c,console.log("Encoding time: "+K+"ms"),J},Z()}function getImageDataFromImage(a){var d,b="string"==typeof a?document.getElementById(a):a,c=document.createElement("canvas");return c.width=b.width,c.height=b.height,d=c.getContext("2d"),d.drawImage(b,0,0),d.getImageData(0,0,c.width,c.height)}
/* megapix-image.js for IOS(iphone5+) drawImage画面扭曲修复  */
!function(){function a(a){var d,e,b=a.naturalWidth,c=a.naturalHeight;return b*c>1048576?(d=document.createElement("canvas"),d.width=d.height=1,e=d.getContext("2d"),e.drawImage(a,-b+1,0),0===e.getImageData(0,0,1,1).data[3]):!1}function b(a,b,c){var e,f,g,h,i,j,k,d=document.createElement("canvas");for(d.width=1,d.height=c,e=d.getContext("2d"),e.drawImage(a,0,0),f=e.getImageData(0,0,1,c).data,g=0,h=c,i=c;i>g;)j=f[4*(i-1)+3],0===j?h=i:g=i,i=h+g>>1;return k=i/c,0===k?1:k}function c(a,b,c){var e=document.createElement("canvas");return d(a,e,b,c),e.toDataURL("image/jpeg",b.quality||.8)}function d(c,d,f,g){var m,n,o,p,q,r,s,t,u,v,w,h=c.naturalWidth,i=c.naturalHeight,j=f.width,k=f.height,l=d.getContext("2d");for(l.save(),e(d,l,j,k,f.orientation),m=a(c),m&&(h/=2,i/=2),n=1024,o=document.createElement("canvas"),o.width=o.height=n,p=o.getContext("2d"),q=g?b(c,h,i):1,r=Math.ceil(n*j/h),s=Math.ceil(n*k/i/q),t=0,u=0;i>t;){for(v=0,w=0;h>v;)p.clearRect(0,0,n,n),p.drawImage(c,-v,-t),l.drawImage(o,0,0,n,n,w,u,r,s),v+=n,w+=r;t+=n,u+=s}l.restore(),o=p=null}function e(a,b,c,d,e){switch(e){case 5:case 6:case 7:case 8:a.width=d,a.height=c;break;default:a.width=c,a.height=d}switch(e){case 2:b.translate(c,0),b.scale(-1,1);break;case 3:b.translate(c,d),b.rotate(Math.PI);break;case 4:b.translate(0,d),b.scale(1,-1);break;case 5:b.rotate(.5*Math.PI),b.scale(1,-1);break;case 6:b.rotate(.5*Math.PI),b.translate(0,-d);break;case 7:b.rotate(.5*Math.PI),b.translate(c,-d),b.scale(-1,1);break;case 8:b.rotate(-.5*Math.PI),b.translate(-c,0)}}function f(a){var b,c,d;if(window.Blob&&a instanceof Blob){if(b=new Image,c=window.URL&&window.URL.createObjectURL?window.URL:window.webkitURL&&window.webkitURL.createObjectURL?window.webkitURL:null,!c)throw Error("No createObjectURL function found to create blob url");b.src=c.createObjectURL(a),this.blob=a,a=b}a.naturalWidth||a.naturalHeight||(d=this,a.onload=function(){var b,c,a=d.imageLoadListeners;if(a)for(d.imageLoadListeners=null,b=0,c=a.length;c>b;b++)a[b]()},this.imageLoadListeners=[]),this.srcImage=a}f.prototype.render=function(a,b,e){var f,g,h,i,j,k,l,m,n,o,p;if(this.imageLoadListeners)return f=this,this.imageLoadListeners.push(function(){f.render(a,b,e)}),void 0;b=b||{},g=this.srcImage.naturalWidth,h=this.srcImage.naturalHeight,i=b.width,j=b.height,k=b.maxWidth,l=b.maxHeight,m=!this.blob||"image/jpeg"===this.blob.type,i&&!j?j=h*i/g<<0:j&&!i?i=g*j/h<<0:(i=g,j=h),k&&i>k&&(i=k,j=h*i/g<<0),l&&j>l&&(j=l,i=g*j/h<<0),n={width:i,height:j};for(o in b)n[o]=b[o];p=a.tagName.toLowerCase(),"img"===p?a.src=c(this.srcImage,n,m):"canvas"===p&&d(this.srcImage,a,n,m),"function"==typeof this.onrender&&this.onrender(a),e&&e()},"function"==typeof define&&define.amd?define([],function(){return f}):this.MegaPixImage=f}();

$(document).ready(function(){
	var t_file_img_jq=$('input:file[imgType]');
	if(t_file_img_jq.length){
		$("<link>").attr({rel: "stylesheet", type: "text/css", href: rootPath+"js/plugin/jcrop/jquery.Jcrop.min.css"}).appendTo("head");
		$("<script>").attr({type: "text/javascript", src: rootPath+"js/plugin/jcrop/jquery.Jcrop.min.js"}).appendTo("head");
	}
	window.setInterval("initImgCrop()", 500);	//每0.5秒钟后执行
}); 
/**
 * 初始化：获得base64——图片文件
 * @param {Object} obj
 * @param {Number} [obj.width] 图片需要压缩的宽度
 * @param {Number} [obj.height] 图片需要压缩的高度（若为null，会跟随比例调整）
 * @param {Number} [obj.x] 裁剪源图的x坐标（默认为0）
 * @param {Number} [obj.y] 裁剪源图的y坐标（默认为0）
 * @param {Number} [obj.quality] 压缩质量，不压缩为1
 * @param {Function} [obj.before(this, blob, file)] 处理前函数,this指向的是input:file
 * @param {Function} obj.success(obj) 处理后函数
 */
function initImgCrop(){
	var t_file_img_jq=$('input:file[imgType]');
	for(var i=0;i<t_file_img_jq.length;i++){
		var t_obj=t_file_img_jq[i];
		_crop_fileServiceInit(t_obj);
	}
}
//每个file初始化处理
function _crop_fileServiceInit(t_obj){
	var t_obj_jq=$(t_obj);
	/*参数*/
	var t_accept=t_obj_jq.attr("accept");
	var t_imgType=t_obj_jq.attr("imgType");	//图片类型（修改后）
	var t_showId=t_obj_jq.attr("showId");	//修改后的图片显示在指定的id上
	var t_isInit=t_obj_jq.attr("isInit");		//是否初始化过
	var t_isUpload=t_obj_jq.attr("isUpload");		//是否压缩后，立即表单提交
	var t_width=t_obj_jq.attr("width");		//宽度
	var t_height=t_obj_jq.attr("height");		//高度
	var t_quality=t_obj_jq.attr("quality");	//质量
	var t_max=t_obj_jq.attr("max");		//一次最多上传的张数（为空不限制）
	var t_imgStyle=t_obj_jq.attr("imgStyle");	//图片style
	var t_imgClass=t_obj_jq.attr("imgClass");	//图片class
	var t_parentStyle=t_obj_jq.attr("parentStyle");	//父style
	var t_parentClass=t_obj_jq.attr("parentClass");	//父class
	/*初始化*/
	switch(t_imgType){
		//认证图片
		case "certification":{
			t_width=!isNaN(t_width)?parseInt(t_width):1200;
			t_height=!isNaN(t_height)?parseInt(t_height):1200;
			t_quality=!isNaN(t_quality)?parseInt(t_quality):0.9;
			break;
		}
		default:{
//			t_width=!isNaN(t_width)?parseInt(t_width):800;
//			t_height=!isNaN(t_height)?parseInt(t_height):600;
			t_quality=!isNaN(t_quality)?parseInt(t_quality):0.85;
			//
			t_width=!isNaN(t_width)?parseInt(t_width):null;
			t_height=!isNaN(t_height)?parseInt(t_height):null;
			break;
		}
	}
	
	t_isUpload=t_isUpload || "false";
	/*其他*/
	var isJoinNew=true;			//是否加入新的input数据到当前文件元素的后面（name为该文件元素name，多个文件以数组形式）
	var t_target_width,t_target_height;		//目标宽度和高度数值
	if(t_imgType=="head"){
		t_target_width=$(window).width();
		t_target_height=$(window).height();
		isJoinNew=false;
		/*固定头像的大图*/
		t_width=120;		
		t_height=120;
		t_isUpload="false";
		t_quality=0.95;
	}else{
		t_target_width=t_width;
		t_target_height=t_height;
	}
	
	//是指定的图片文件类型
	if(t_imgType!=null && t_isInit!="true"){
		var t_soleId=local_getSoleId("crop_hidden_param_");		//当前唯一id
		var t_crop_hidden_param_id="crop_hidden_param_"+t_soleId;	//隐藏参数div的id
		t_obj_jq.attr("crop_hidden_param_id",t_crop_hidden_param_id);	
		t_obj_jq.attr("soleId",t_soleId);
		 if(t_accept==null){
//			 t_obj_jq.attr("accept",".jpeg,.jpg,.png,.gif;");		//初始化所有文件类型为：图片文件
			 t_obj_jq.attr("accept","image/*");		//初始化所有文件类型为：图片文件
		 }
		 var t_showId_jq=$("#"+t_showId);		//显示到指定元素
		 var t_json={
			obj:t_obj,
			showId:t_showId,
			sourceImgType:t_imgType,
			imgType:t_imgType,
			imgStyle:t_imgStyle,
			imgClass:t_imgClass,
			parentStyle:t_parentStyle,
			parentClass:t_parentClass,
			sourceTargetWidth:t_width,
			sourceTargetHeight:t_height,
			width: t_target_width,
			height:t_target_height,
			quality: t_quality,
			isJoinNew:isJoinNew,
			isUpload:t_isUpload,
			//before: function (that, blob) {},
			success: function (result) {
				var t_isJoinNew=t_json.isJoinNew;
				if(t_showId!=null){
					var t_index=result.index;
					var t_fileSize=result.fileSize;		//压缩的文件总数
					var t_base64=result.base64;
					/*图片*/
					var img = new Image();
					var t_img_jq=$(img);
					img.src = t_base64;		//图片数据加载
					//
					var t_soleId=new Date().getTime();
					var t_div_hidden_id='local_layer_hidden_'+t_soleId;		//隐藏属性区id
					switch(t_imgType){
						//头像
						case "head":{
							//新层
							local_layer('头像裁剪',function(elem,t_content_jq,t_index){
								var t_img_id="local_layer_head_"+t_soleId;	//当前图片id
								t_img_jq.css("max-width","100%");		//设置最大宽度为：100%
								t_img_jq.attr("id",t_img_id);
								/*************内容**********/
								var t_local_img_html=$("<div>").append(img).html();
								var t_layer_html='<div>';
								/*内容*/
								t_layer_html+='<div style="width:100%;height:100%;overflow:hidden;background-color: #000;position: fixed;">';
								t_layer_html+='<table style="text-align:center;height:100%;margin:0 auto;"><tr><td style="text-align:center;vertical-align: middle;">'+t_local_img_html+'</td></tr></table>';
								t_layer_html+='</div>';
								/*隐藏属性*/
								t_layer_html+='<div id="'+t_div_hidden_id+'"></div>';
								//结尾
								t_layer_html+='</div>';
								
								t_content_jq.html(t_layer_html);	//渲染
								local_crop_head($("#"+t_img_id),t_div_hidden_id);		//头像裁剪
							},function(btnObj,index){
								$(btnObj).attr("hiddenId",t_div_hidden_id);
								var t_newJson = $.extend(true, {}, t_json);
								local_confirm_tailor_head(btnObj,t_newJson,result);
							});
							break;
						}
					}
				}
				local_joinImgData(t_json,result,t_isUpload,null,null);		//加入新的图片数据、参数
				local_waitClose();
			}
		}
		/*所有jquery对象定义change事件*/
		 t_obj_jq.on('change', function() {
			local_waitShow();
			var t_fileList=this.files;
			var t_size=t_fileList.length;
			//是：头像
			if(t_imgType=="head"){
				t_size=1;
			}else{
				t_showId_jq.html("");		//清空
				var t_crop_hidden_param_jq=$("#"+t_crop_hidden_param_id);
				if(t_crop_hidden_param_jq.length<=0){
					var t_after_hidden_div_html='<div id="'+t_crop_hidden_param_id+'" style="display: none"></div>';
					t_obj_jq.after(t_after_hidden_div_html);
				}else{
					t_crop_hidden_param_jq.html("");		//清空参数
				}
			}
			if(t_max!=null){
				var t_t_max=parseInt(t_max);
				if(t_size>t_t_max){
					t_size=t_t_max;
					local_prompt(false,"上传数","每次最多上传【"+t_max+"】张,默认选取前"+t_max+"张");
				}
			}
			for(var i=0;i<t_size;i++){
				var file=t_fileList[i];
				var URL = window.URL || window.webkitURL;
				var blobURL = URL.createObjectURL(file);
				// 执行前函数
				if ($.isFunction(t_json.before)) {
					t_json.before(this, blobURL, file)
				};
				_getModifyImgBase64(t_json,blobURL, file,t_size,i);
			}
			this.value = ''; // 清空临时数据
		});
		t_obj_jq.attr("isInit","true");
	}
}
/**
 * 获取修改后的图片base64
 * @param blob 通过file获得的二进制URL
 */
function _getModifyImgBase64(obj,blobURL,file,fileSize,index) {
	var img = new Image();
	img.src = blobURL;
	img.onload = function() {
		var that = this;
		var t_imgType=obj.imgType;		//修改的图片类型
		var w = that.width;		//源图宽
		var h = that.height;		//源图高
		var t_obj_width=obj.width || w;
		var t_obj_height=obj.height || h;
		var t_obj_cropWidth=obj.cropWidth || w;	//源图自x坐标开始剪切的宽度
		var t_obj_cropHeight=obj.cropHeight || h;	//源图自x坐标开始剪切的高度
		var x=obj.x || 0;
		var y=obj.y || 0;
		var t_maxWidth,t_maxHeight;
		var imgWidth,imgHeight;
		switch(t_imgType){
			//规范（固定最大宽和高）
			case "norm":case "certification":{
				var t_maxWidth = t_obj_width || w;
				var t_maxHeight = t_obj_height || h;
				var scaling=1.0;		//默认一个比例
				if(w>t_maxWidth || h>t_maxHeight){
					//缩放比例(根据高)
					if(w>h){
						scaling=w/t_maxWidth;
					}else{
						scaling=h/t_maxHeight;
					}
				}
				imgWidth=w/scaling;
				imgHeight=h/scaling;
				break;
			}
			//缩放、头像
			case "zoom":case "head":{
				var t_scaling_width=w/t_obj_width;			//缩放比例——宽
				var t_scaling_height=h/t_obj_height;			//缩放比例——高
				var t_scaling=1;
				if(t_scaling_width>t_scaling_height){
					t_scaling=t_scaling_width;
				}else{
					t_scaling=t_scaling_height;
				}
				if(t_scaling>1){
					imgWidth=w/t_scaling;
					imgHeight=h/t_scaling;
				}else{
					imgWidth=w;
					imgHeight=h;
				}
				break;
			}
			//其他（取固定的宽和高）
			default:{
				imgWidth=t_obj_width;
				imgHeight=t_obj_height;
			}
		}		
		//生成canvas
		var canvas = document.createElement('canvas');
		var ctx = canvas.getContext('2d');
		$(canvas).attr({
			width: imgWidth,
			height: imgHeight
		}); 
		//alert(x+"_"+y+"；"+t_obj_cropWidth+"_"+t_obj_cropHeight+";"+imgWidth+"_"+imgHeight);
		ctx.drawImage(that, x, y, t_obj_cropWidth, t_obj_cropHeight,0,0,imgWidth,imgHeight);

		/**
		 * 生成base64
		 * 兼容修复移动设备需要引入mobileBUGFix.js
		 */
		var base64 = canvas.toDataURL('image/jpeg', obj.quality || 0.8);
		// 修复IOS
		if (navigator.userAgent.match(/iphone/i)) {
			var mpImg = new MegaPixImage(img);
			mpImg.render(canvas, {
				maxWidth: imgWidth,
				maxHeight: imgHeight,
				quality: obj.quality || 0.8
			});
			base64 = canvas.toDataURL('image/jpeg', obj.quality || 0.8);
		}
		// 修复android
		if (navigator.userAgent.match(/Android/i)) {
			var encoder = new JPEGEncoder();
			base64 = encoder.encode(ctx.getImageData(0, 0, imgWidth, imgHeight), obj.quality * 100 || 80);
		}
		//dataURL 的格式为 “data:image/png;base64,****”,逗号之前都是一些说明性的文字，我们只需要逗号之后的就行了
		var t_data = base64.split(',')[1];
		var t_base64_atob=window.atob(t_data);
		var ia = new Uint8Array(t_base64_atob.length);
		for (var i = 0; i < t_base64_atob.length; i++) {
		    ia[i] = t_base64_atob.charCodeAt(i);
		}
		//canvas.toDataURL 返回的默认格式就是 image/png
		var t_zoom_contentType="image/jpeg";
		var blob_compress = new Blob([ia], {
		    type: t_zoom_contentType
		});
		// 生成结果
		var result = {
			index:index,
			fileSize:fileSize,
			contentType:t_zoom_contentType,
			blob:blob_compress,		//压缩、修改后的图片blob
			base64: base64,
			clearBase64: base64.substr(base64.indexOf(',') + 1)		//图片数据（不带说明文字）
		};
		// 执行后函数
		obj.success(result);
	};
}
/*************头像*************/
//调用crop
function local_crop_head(obj,t_div_hidden_id){
	var obj_jq=$(obj);
	var t_config_json={};
	var jcrop_api;
	//是否存在方法：更新预览
	if(window.updatePreview){
		t_config_json["onChange"]=updatePreview;		//改变
		t_config_json["onSelect"]=updatePreview;		//选择
	}else{
		t_config_json["onChange"]=function(c){
			local_updatePreview(c,t_div_hidden_id);		//改变
		};
		t_config_json["onSelect"]=function(c){
			local_updatePreview(c,t_div_hidden_id);		///选择
		};
	}
	t_config_json["baseClass"]="jcrop";		//基本class
	t_config_json["aspectRatio"]=1;			//纵横比
	obj_jq.Jcrop(t_config_json,function () {
		var bounds = this.getBounds();
		var bound_width = bounds[0];		//宽
		var boundy_height = bounds[1];		//高
		jcrop_api = this;
	});
	if(jcrop_api!=null){
		jcrop_api.setImage(obj_jq.attr("src"));
	}
}
//当前的：更新预览
function local_updatePreview(c,t_div_hidden_id){
	var imgX=c.x;
	var imgY=c.y;
	var imgWidth=c.w;
	var imgHeight=c.h;
	var t_div_hidden_jq=$("#"+t_div_hidden_id);
	t_div_hidden_jq.html("");
	if(imgX!=null && imgY!=null && imgWidth!=null && imgHeight!=null){
		$("#imgX").val(imgX);
		$("#imgY").val(imgY);
		$("#imgWidth").val(imgWidth);
		$("#imgHeight").val(imgHeight);
		/*当前层的参数*/
		var t_html='<input type="hidden" name="imgX" value="'+imgX+'"/>';
		t_html+='<input type="hidden" name="imgY" value="'+imgY+'"/>';
		t_html+='<input type="hidden" name="imgWidth" value="'+imgWidth+'"/>';
		t_html+='<input type="hidden" name="imgHeight" value="'+imgHeight+'"/>';
		t_div_hidden_jq.html(t_html);
	}
}
//渲染、加入新的图片数据
function local_joinImgData(t_json,result,t_isUpload,title,content,successFn,failureFn){
	var t_obj=t_json.obj;
	var t_isJoinNew=t_json.isJoinNew;
	var t_index=result.index;
	var t_fileSize=result.fileSize;		//压缩的文件总数
	var t_base64=result.base64;
	var t_contentType=result.contentType;
	//是否立即上传
	if(t_isUpload=="true"){
		var t_blob=result.blob;
		local_ajax(t_obj,t_blob,title,content,function(requestResult){
			if(successFn!=null){successFn();}
			local_renderModifyImg(t_json,result,requestResult);		//渲染图片
		},function(){
			if(failureFn!=null){failureFn();}
		});
	//是否加入图片修改后的input元素
	}else if(t_isJoinNew==true){
		if(successFn!=null){successFn();}
		local_renderModifyImg(t_json,result,null);		//渲染图片
		var t_obj_jq=$(t_obj);
		var t_input_file_name=t_obj_jq.attr("name");		//当前文件元素的name
		var t_input_file_requestName=t_obj_jq.attr("requestName");		//指定的请求参数名
		var t_input_file_imgType=t_obj_jq.attr("imgType");		//图片类型
		var t_crop_hidden_param_id=t_obj_jq.attr("crop_hidden_param_id");		//隐藏参数div
		var t_new_name=t_input_file_name;
		var t_new_contentType;
		if(t_input_file_requestName!=null && t_input_file_requestName.length>0){
			t_new_name=t_input_file_requestName;
			var t_arr_index=t_new_name.indexOf("[");
			var t_arrNum='';
			if(t_arr_index!=-1){
				t_new_contentType=t_new_name.substring(0,t_arr_index);
				var t_arrNum=t_new_name.substring(t_arr_index);
			}else{
				t_fileContentType=t_new_name;
			}
			t_new_contentType+="ContentType"+t_arrNum;
		}else{
//			if(t_fileSize>1){
//				t_new_name=t_input_file_name+"["+t_index+"]";
//			}
			t_new_name=t_input_file_name+"["+t_index+"]";
			t_new_contentType=t_input_file_name+"ContentType["+t_index+"]";
		}
		var t_modify_file_html='<input type="hidden" name="'+t_new_name+'" belongName="'+t_input_file_name+'" value="'+t_base64+'" imgType="'+t_input_file_imgType+'" disabled="disabled" />';
		t_modify_file_html+='<input type="hidden" name="'+t_new_contentType+'" imgContentType="'+t_contentType+'" value="'+t_contentType+'"  />';
		var t_crop_hidden_param_jq=$("#"+t_crop_hidden_param_id);
		t_crop_hidden_param_jq.append(t_modify_file_html);
	}
}
//渲染修改后的图片（t_requestResult=请求后返回的结果数据，通常为json数据）
function local_renderModifyImg(t_json,t_result,t_requestResult){
	var t_base64=t_result.base64;
	var t_fileSize=t_result.fileSize;		//压缩的文件总数
	/*图片*/
	var t_img = new Image();
	var t_img_jq=$(t_img);
	t_img.src = t_base64;		//图片数据加载
	var t_showId=t_json.showId;
	var t_file_obj=t_json.obj;
	var t_file_obj_jq=$(t_file_obj);
	var t_imgCallBack=t_file_obj_jq.attr("imgCallBack");		//回调方法名
	//html
	var t_imgStyle=t_json.imgStyle;
	var t_imgClass=t_json.imgClass;
	var t_parentStyle=t_json.parentStyle;
	var t_parentClass=t_json.parentClass;
	if(t_imgStyle!=null){
		t_img_jq.attr("style",t_imgStyle);
	}
	if(t_imgClass!=null){
		t_img_jq.attr("class",t_imgClass);
	}
	/*图片包装元素（父元素）*/
	var t_parent_jq=$('<t onClick="local_viewBigImg(\''+t_base64+'\')"></t>');
	if(t_parentStyle!=null){
		t_parent_jq.attr("style",t_parentStyle);
	//文件数大于1个
	}else if(t_fileSize>1){
		t_parent_jq.attr("style","margin-left:5px;");
	}
	if(t_parentClass!=null){
		t_parent_jq.attr("class",t_parentClass);
	}
	t_parent_jq.css("cursor","pointer");
	t_parent_jq.html(t_img_jq);
	//若回调方法存在
	if(t_imgCallBack!=null){
		var t_t_html=$("<p>").append(t_parent_jq).html();
		var t_t_img_html=$("<p>").append(t_img_jq).html();
		eval(t_imgCallBack+"(t_img,t_t_html,t_requestResult);");
	}else{
		//渲染至指定的id
		var t_showId_jq=$("#"+t_showId);
		/*渲染*/
		t_showId_jq.append(t_parent_jq);
	}
}
//
//确定裁剪的头像（result=源压缩后的图片数据json）
function local_confirm_tailor_head(btn_obj,t_json,result){
	var t_file_obj=t_json.obj;
	var t_file_obj_jq=$(t_file_obj);
	var t_btn_obj_jq=$(btn_obj);
	var t_layerId=t_btn_obj_jq.attr("layerId");
	var t_hiddenId=t_btn_obj_jq.attr("hiddenId");
	var t_isUpload=t_file_obj_jq.attr("isUpload");		//是否压缩后，立即表单提交
	var t_crop_hidden_param_id=t_file_obj_jq.attr("crop_hidden_param_id");		//隐藏属性div的id
	
	var t_div_hidden_jq=$("#"+t_hiddenId);
	var t_imgX=t_div_hidden_jq.find("input[name='imgX']").val();
	var t_imgY=t_div_hidden_jq.find("input[name='imgY']").val();
	var t_imgWidth=t_div_hidden_jq.find("input[name='imgWidth']").val();
	var t_imgHeight=t_div_hidden_jq.find("input[name='imgHeight']").val();
	/*初始化*/
	t_isUpload=t_isUpload || "false";
	
	var t_sourceTargetWidth=t_json["sourceTargetWidth"];		//源目标宽度
	var t_sourceTargetHeight=t_json["sourceTargetHeight"];		//~~~~高度
	t_json["imgType"]="custom";		//自定义类型（宽和高）
	t_json["width"]=t_sourceTargetWidth;
	t_json["height"]=t_sourceTargetHeight;
	t_json["x"]=t_imgX;		//裁剪后的x坐标
	t_json["y"]=t_imgY;		//裁剪后的y坐标
	t_json["cropWidth"]=t_imgWidth;		//裁剪后的width
	t_json["cropHeight"]=t_imgHeight;		//裁剪后的height
	t_json["quality"]=0.9;		//前面已压缩，这里不压缩（稍微压缩）
	t_json["before"]=null;
	t_json["isJoinNew"]=true;
	t_json["success"]=function(t_result,fileSize){
		var t_showId=t_json.showId;
		var t_showId_jq=$("#"+t_showId);		//显示图片的元素
		//加入新的图片数据、参数	
		local_joinImgData(t_json,t_result,t_isUpload,"上传","正在上传头像",function(){
			t_showId_jq.html("");
			var t_crop_hidden_param_jq=$("#"+t_crop_hidden_param_id);
			if(t_crop_hidden_param_jq.length<=0){
				var t_after_hidden_div_html='<div id="'+t_crop_hidden_param_id+'" style="display: none"></div>';
				t_crop_hidden_param_jq.after(t_after_hidden_div_html);
			}else{
				t_crop_hidden_param_jq.html("");		//清空参数
			}
		});		
	};
	local_waitShow("裁剪","正在裁剪头像");
	var blob = result.blob;		//源压缩后的blob
	var URL = window.URL || window.webkitURL;
	var t_blobURL = URL.createObjectURL(blob);
	_getModifyImgBase64(t_json,t_blobURL, null,1,0);
	local_waitClose();
	local_layerClose(t_layerId);
}
//ajax提交表单（t_t_obj=文件）
function local_ajax(t_t_obj,t_blob,title,content,successFn,failureFn,errorFn){
	var t_t_obj_jq=$(t_t_obj);
	var t_input_file_name=t_t_obj_jq.attr("name");		//当前文件元素的name
	/*表单*/
	var t_form_obj=t_t_obj.form;
	var t_form_jq=$(t_form_obj);
	var t_form_url=t_form_jq.attr("action");
	var t_formData = new FormData(t_form_obj);
	t_formData.append(t_input_file_name,t_blob);
	//ajax发送
	ajaxUpload(t_form_url,t_formData,successFn,failureFn,errorFn,true,title || "上传",content || "正在上传图片","上传成功");
}
//获取（生成）唯一值做为ID，不返回t_prefix
function local_getSoleId(t_prefix){
	var num=50;		//循环次数
	var t_form_id=null;
	var t_obj_jq=null;
	var t_sole=0;
	while(t_form_id==null || (t_obj_jq.length>0 && num>0)){
		t_sole=Math.round(Math.random() * 1000000);
		t_form_id=t_prefix+t_sole;		//新表单ID
		t_obj_jq=$("#"+t_form_id);
	}
	return t_sole;
}
/********************插件（层）******************/
//加载图
function local_waitShow(title,content){
	waitShow(title || "加载图片",content || "正在加载图片");
}
//关闭加载
function local_waitClose(){
	waitClose();
}
//提示
function local_prompt(isSuccess,title,content){
	promptDialog(isSuccess,title,content);
}
//新层关闭
function local_layerClose(index){
	layer.close(index);
}
//新版面（新层）——当前
function local_layer(title,successFn,okFn,width,height){
	return _layer(title,successFn,okFn,width,height);
}
//查看大图
function local_viewBigImg(src){
	htmlDialog("查看大图","<div style='text-align:center;'><img src='"+src+"' style='max-width:100%;max-height:100%;'/></div>");
}