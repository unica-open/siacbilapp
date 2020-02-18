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
					<s:include value="/jsp/include/messaggi.jsp" />
					<form method="post">
						<h3>Risultati di ricerca registro iva</h3>
						<h4><span id="id_num_result" class="num_result"></span></h4>
						
						<table class="table table-hover tab_left dataTable" id="risultatiRicercaRegistroIva">
							<thead>
								<tr role="row">
									<th scope="col" role="columnheader">Codice</th>
									<th scope="col" role="columnheader">Descrizione</th>
									<th scope="col" role="columnheader">Gruppo Attivit&agrave; iva</th>
									<th scope="col" role="columnheader">Tipo registro iva</th>
									<%-- SIAC-6276 CR-1179B --%>
									<th scope="col" role="columnheader">Liquidazione IVA</th>
									<th scope="col" role="columnheader">Bloccato</th>
									<th scope="col" role="columnheader" class="tab_Right" >&nbsp;</th>
								</tr>
							<tbody>
							</tbody>
						</table>

						<s:hidden id="HIDDEN_startPosition" name="startPosition" value="%{savedDisplayStart}" />
						
						<%-- Modale ELIMINA --%>
						<div aria-hidden="true" aria-labelledby="msgEliminaLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleConferma">
							<div class="modal-body">
								<div class="alert alert-error alert-persistent">
									<button data-hide="alert" class="close" type="button">&times;</button>
									<p><strong>Attenzione!</strong></p>
									<p><strong>Elemento selezionato: <span id="SPAN_registroSelezionato"></span></strong></p>
									<p class="descrizione-azione-da-confermare">Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
								</div>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn" id="pulsanteNo">no, indietro</button>
								<button type="button" class="btn btn-primary" id="pulsanteSi">s&igrave;, prosegui</button>
							</div>
						</div>
						
						<%-- Modale CONSULTA --%>
						<div aria-hidden="true" aria-labelledby="msgConsultaGruppoLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleConsulta">
							<div class="modal-header">
								<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
								<h4 class="nostep-pane">Consulta</h4>
							</div>
							<div class="modal-body">
								<div class="boxOrSpan2">
									<div class="boxOrInline">
										<p>Registro Iva</p>
										<ul class="htmlelt">
											<li>
												<dfn>Gruppo attivit&agrave; iva</dfn>
												<dl id="DL_gruppoAttivitaIva"></dl>
											</li>
											<li>
												<dfn>Tipo registro iva</dfn>
												<dl id="DL_tipoRegistroIva"></dl>
											</li>
											<li>
												<dfn>Codice</dfn>
												<dl id="DL_codice"></dl>
											</li>
											<li>
												<dfn>Descrizione</dfn>
												<dl id="DL_descrizione"></dl>
											</li>
											<li>
												<dfn>Liquidazione IVA</dfn>
												<dl id="DL_liquidazione"></dl>
											</li>
										</ul>
									</div>
								</div>
							</div>
							<div class="modal-footer">
								<button aria-hidden="true" data-dismiss="modal" class="btn btn-primary">chiudi</button>
							</div>
						</div>
						<div class="Border_line"></div>
						<p>
							<s:include value="/jsp/include/indietro.jsp" />
						</p>
					</form>
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}attivitaIva/registroIva/risultatiRicerca.js"></script>
</body>
</html>