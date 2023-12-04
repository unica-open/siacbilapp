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
	
	<!-- Corpo pagina-->

	<!-- TABELLE RIEPILOGO -->
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<form method="post" action="#">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3><span id="id_num_result" class="num_result"></span> Risultati trovati</h3>

					<!-- TABELLE RIEPILOGO con azioni -->
					<table class="table table-striped table-bordered table-hover dataTable" id="risultatiricerca" summary="...." >
						<thead>
							<tr>
								<th scope="col">Capitolo</th>
								<th scope="col">Stato</th>
								<th scope="col">Classificazione</th>
								<th scope="col">Competenza</th>
								<th scope="col">Residuo</th>
								<th scope="col">Cassa</th>
								<th scope="col"><abbr title="Struttura Amministrativa Responsabile">Strutt. Amm. Resp.</abbr></th>
								<th scope="col"><abbr title="Piano dei Conti">P.d.C.</abbr> finanziario</th>
								<th scope="col">Azioni</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
							<tr>
								<th scope="col">Capitolo</th>
								<th scope="col">Stato</th>
								<th scope="col">Classificazione</th>
									<th scope="col" class="text-right">
										<s:property value="totaleImporti.stanziamento"/>
									<th scope="col" class="text-right">
										<s:property value="totaleImporti.stanziamentoResiduo"/>
									</th>
									<th scope="col" class="text-right">
										<s:property value="totaleImporti.stanziamentoCassa"/>
									</th>
								<th scope="col"><abbr title="Struttura Amministrativa Responsabile">Strutt. Amm. Resp.</abbr></th>
								<th scope="col"><abbr title="Piano dei Conti">P.d.C.</abbr> finanziario</th>
								<th scope="col">Azioni</th>
							</tr>
						</tfoot>
					</table>
					<s:hidden id="HIDDEN_startPosition" name="startPosition" value="%{savedDisplayStart}" />
					
					<!-- Modale ELIMINA -->
					<div id="msgElimina" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgEliminaLabel" aria-hidden="true">
						<s:hidden id="HIDDEN_UidDaEliminare" name="uidDaEliminare" />
						<div class="modal-body">
							<div class="alert alert-error">
								<button type="button" class="close" data-hide="alert">&times;</button>
								<p><strong>Attenzione!</strong></p>
								<p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
							</div>
						</div>
						<div class="modal-footer">
							<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
							<button class="btn btn-primary" formmethod="post" type="submit" formaction="risultatiRicercaCapUscitaPrevisioneElimina.do">si, prosegui</button>
						</div>
					</div>  
					<!-- /Modale ELIMINA -->
				
					<!-- Modale ANNULLA -->
						<div id="msgAnnulla" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
							<s:hidden id="HIDDEN_UidDaAnnullare" name="uidDaAnnullare" />
							<div class="modal-body">
								<div class="alert alert-error">
									<button type="button" class="close" data-hide="alert">&times;</button>
									<p><strong>Attenzione!</strong></p>
									<p>Stai per annullare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
								</div>
							</div>
							<div class="modal-footer">
								<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
								<button class="btn btn-primary" formmethod="post" type="submit" formaction="risultatiRicercaCapUscitaPrevisioneAnnulla.do">si, prosegui</button>
							</div>
						</div>
						<!-- /Modale ANNULLA -->
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
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/risultatiRicerca.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitoloUscitaPrevisione/risultatiRicerca.js"></script>
</body>
</html>