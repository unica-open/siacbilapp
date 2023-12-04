<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<s:include value="/jsp/include/head.jsp" />
</head>

<body>
	<s:include value="/jsp/include/header.jsp" />
	
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<s:include value="/jsp/include/messaggi.jsp"/>
					
					<form method="post" action="#">
						<h3>Risultati di ricerca Registro</h3>
						<h4><s:property value="riepilogoRicerca"/></h4>
						
						<h4><span id="id_num_result" class="num_result"></span></h4>
							<table class="table table-hover tab_left dataTable" id="risultatiRicercaRegistrazioniMovfin" summary="....">
							<thead>
								<tr>
									<th scope="col"><abbr title="Numero">N.</abbr> movimento</th>
									<th scope="col">Evento</th>
									<th scope="col">Stato richiesta</th>
									<th scope="col">Data registrazione</th>
									<th scope="col">Conto finanziario</th>
									<th scope="col">Conto finanziario aggiornato</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
						<s:hidden id="HIDDEN_startPosition" name="startPosition" value="%{savedDisplayStart}" />

						<p>
							<s:include value="/jsp/include/indietro.jsp" />
							<span class="pull-right">
								<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#modaleConfermaRegistrazioneMassiva">Crea prime note</button>
							</span>
						</p>
					</form>
					<s:include value="/jsp/contabilitaGenerale/registrazione/include/modaleConfermaRegistrazioneMassiva.jsp" />
				</div>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/contabilitaGenerale/registrazione/risultatiRicercaRegistrazioneMassivaMovFin.js"></script>
</body>
</html>