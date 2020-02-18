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

	<!-- TABELLE RIEPILOGO -->
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<s:include value="/jsp/include/messaggi.jsp" />
					<form method="post">
						<h3>Risultati di ricerca gruppo attività iva</h3>
						<h4><span id="id_num_result" class="num_result"></span></h4>
						
						<table class="table table-hover tab_left dataTable" id="risultatiRicercaGruppoAttivitaIva">
							<thead>
								<tr role="row">
									<th scope="col" role="columnheader">Codice</th>
									<th scope="col" role="columnheader">Descrizione</th>
									<th scope="col" role="columnheader">Tipo chiusura</th>
									<th scope="col" role="columnheader">Tipo attivit&agrave;</th>
									<th scope="col" role="columnheader" class="tab_Right" >&#37; Pro-rata</th>
									<th scope="col" role="columnheader" class="tab_Right" >&nbsp;</th>
								</tr>
							<tbody>
							</tbody>
						</table>

						<s:hidden id="HIDDEN_startPosition" name="startPosition" value="%{savedDisplayStart}" />
						
						<%-- Modale ELIMINA --%>
						<div aria-hidden="true" aria-labelledby="msgEliminaLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleElimina">
							<div class="modal-body">
								<div class="alert alert-error">
									<button data-hide="alert" class="close" type="button">&times;</button>
									<p><strong>Attenzione!</strong></p>
									<p><strong>Elemento selezionato: <span id="SPAN_elimina"></span></strong></p>
									<p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
								</div>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn" id="pulsanteNoElimina">no, indietro</button>
								<button type="button" class="btn btn-primary" id="pulsanteSiElimina">s&igrave;, prosegui</button>
							</div>
						</div>
						
						<div class="Border_line"></div>
						<p>
							<s:include value="/jsp/include/indietro.jsp" />
						</p>
					</form>
					<div id="placeholderConsultazione"></div>
				</div>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}attivitaIva/gruppoAttivitaIva/risultatiRicerca.js"></script>
</body>
</html>