<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
					<s:form action="risultatiRicercaStornoUEBAggiorna" novalidate="novalidate" id="formRedirezioneAggiornamento">
						<s:include value="/jsp/include/messaggi.jsp" />
						<h3><span id="id_num_result" class="num_result"></span> Risultati di ricerca storni UEB</h3> 
						<s:hidden id="HIDDEN_startPosition" value="%{savedDisplayStart}" />
						<table class="table table-hover dataTable" id="risultatiRicercaStorniUEB">
							<thead>
								<tr>
									<th></th>
									<th scope="col">Numero storno</th>
									<th scope="col">Capitolo/UEB Sorgente</th>
									<th scope="col">Capitolo/UEB destinazione</th>
									<th scope="col">Provvedimento</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
							<tfoot>
							</tfoot>
						</table>
						<p>
							<s:include value="/jsp/include/indietro.jsp" />
							<s:reset cssClass="btn btn-link" value="annulla" />
							<s:submit cssClass="btn btn-primary pull-right" value="aggiorna" />
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>

	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}storniUEB/storni.js"></script>
	<script type="text/javascript" src="${jspath}storniUEB/risultatiRicerca.js"></script>

</body>
</html>