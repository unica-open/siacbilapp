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
	
	
	
		<%-- Pagina JSP vera e propria --%>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:include value="/jsp/include/messaggi.jsp" />

				<div class="step-content">
					<div class="step-pane active" >
							
							<div class="alert alert-warning hide" id="alertErrori">
								<button type="button" class="close" data-hide="alert">&times;</button>
								<strong>Attenzione!!</strong><br>
								<ul id="errori"></ul>
							</div>
	
							<div>
							<fieldset class="form-horizontal" id="fieldset_inserimentoVariazioneImporti">
									<div class="control-group">
										<button type="button" class="btn" id="button_inserisciModifica" style="margin-bottom:10px;"  >Nuova Riga Componente</button>
										<table class="table table-hover table-condensed table-bordered"
										id="tabellaInserimentoNuovoComponente" ></table>
									</div>

								</fieldset>								
							</div>
							
	
							<p>
								<s:a cssClass="btn" action="inserisciCapUscitaPrevisione"
								id="pulsanteRedirezioneIndietro">indietro</s:a>
							</p>
							
							
							
						
					</div>
				</div>
				
				
				<!--SIAC 6881 -->
					<%-- Modale Inserimento Modifiche --%>
					<div id="insertEdit" class="modal hide fade" tabindex="-1" role="dialog"
						aria-labelledby="myEditModalLabel" aria-hidden="true">
						<div class="overlay-modale">
							<div class="row-fluid">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal"
										aria-hidden="true">×</button>
									<h3 id="titoloModaleInserisciModifica"></h3>
									<%--Modifica Stanziamenti Capitolo/UEB xxxxxxxxxxx</h3>--%>
								</div>
								<div class="modal-body">
									<div class="alert alert-error hide" id="ERRORI_modaleInsertStanziamenti">
										<button type="button" class="close" data-hide="alert">&times;</button>
										<strong>Attenzione!!</strong><br>
										<ul>
										</ul>
									</div>

									<!-- SIAC 6881 - Modale nuova -->
									<div id="idBodyInsertModal"></div>
									<!-- SIAC 6881 - Modale nuova  END-->

									
								</div>
								<div class="modal-footer">
									<button type="button" class="btn" data-dismiss="modal"
										aria-hidden="true">chiudi</button>
									<button type="button" class="btn btn-primary" id="INSERT_modifica">inserisci
										modifica</button>
								</div>
							</div>
						</div>
					</div>
				
			</div>
		</div>
	</div>
	
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}capitoloUscitaPrevisione/inserisciStanziamenti.js"></script>
</body>
</html>