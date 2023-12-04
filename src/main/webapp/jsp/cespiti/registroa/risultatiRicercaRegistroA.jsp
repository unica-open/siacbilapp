<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<s:form cssClass="form-horizontal">
						<h3 id="titolo">Ricerca registro prime note definitive verso inventario contabile</h3>
						<s:include value="/jsp/include/messaggi.jsp" />
						<div class="step-content">
						<br/>
						<fieldset class="form-horizontal">
							<h4><span id="id_num_result" class="num_result"></span></h4>
							<s:include value="/jsp/cespiti/registroa/include/tabellaPrimaNoteRegistroA.jsp"/>
						</fieldset>
						</div>
						<p class="margin-medium">
							<s:include value="/jsp/include/indietro.jsp" />
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>
	<s:hidden id="HIDDEN_savedDisplayStart" name="startPosition" value="%{savedDisplayStart}" />
	
	<s:include value="/jsp/cespiti/include/modaleEliminazione.jsp"/>	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<s:include value="/jsp/cespiti/primaNotaLibera/include/modaleDettaglioCespiti.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/cespiti/registroa/risultatiRicercaRegistroA.js"></script>
</body>
</html>