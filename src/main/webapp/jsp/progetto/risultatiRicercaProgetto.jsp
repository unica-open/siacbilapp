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
					<s:include value="/jsp/include/messaggi.jsp"/>
					<form method="post" action="#">
						<h3><span id="id_num_result" class="num_result"></span></h3>
						
						<table class="table tab_left table-hover dataTable" id="risultatiRicercaProgetto" summary="...." >
							<thead>
								<tr>
									<th scope="col">Codice Progetto</th>
									<th scope="col">Stato</th>
									<th scope="col">Provvedimento</th>
									<th scope="col">Ambito</th>
									<th class="tab_Right" scope="col"></th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
						
						<s:hidden id="HIDDEN_startPosition" name="startPosition" value="%{savedDisplayStart}" />

						<div id="msgAnnulla" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
							<s:hidden id="HIDDEN_UidDaAnnullare" name="uidDaAnnullare" />
							<div class="modal-body">
								<div class="alert alert-error">
									<button type="button" class="close" data-hide="alert">&times;</button>
									<p><strong>Attenzione!</strong></p>
									<p>
										Stai per annullare l'elemento selezionato: sei sicuro di voler proseguire?
									</p>
								</div>
							</div>
							<div class="modal-footer">
								<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
								<button class="btn btn-primary" formmethod="post" type="submit" formaction="risultatiRicercaProgettoAnnulla.do">si, prosegui</button>
							</div>
						</div>
						
						<div id="msgRiattiva" aria-hidden="false" aria-labelledby="msgRiattivaLabel" role="dialog" tabindex="-1" class="modal hide fade">
							<s:hidden id="HIDDEN_UidDaRiattivare" name="uidDaRiattivare" />
							<div class="modal-body">
								<div class="alert alert-error">
									<button data-hide="alert" class="close" type="button">×</button>
									<p><strong>Attenzione!</strong></p>
									<p>
										Stai per RIATTIVARE l'elemento selezionato, questo cambierà lo stato dell'elemento: sei sicuro di voler proseguire?
									</p>
								</div>
							</div>
							<div class="modal-footer">
								<button aria-hidden="true" data-dismiss="modal" class="btn">no, indietro</button>
								<button class="btn btn-primary" formmethod="post" type="submit" formaction="risultatiRicercaProgettoRiattiva.do">si, prosegui</button>
							</div>
						</div>
						
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
	<script type="text/javascript" src="${jspath}progetto/risultatiRicerca.js"></script>
</body>
</html>