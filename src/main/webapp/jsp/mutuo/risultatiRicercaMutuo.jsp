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
						
						<table class="table tab_left table-hover dataTable" id="risultatiRicercaMutuo" summary="...." >
							<thead>
								<tr>
									<th scope="col"></th>
									<th scope="col">Numero</th>
									<th scope="col">Tipo Tasso</th>
									<th scope="col">Stato</th>
									<th scope="col">Periodo</th>
									<th scope="col">Tasso Interesse</th>
									<th scope="col">Euribor</th>
									<th scope="col">Spread</th>
									<th scope="col">Provvedimento</th>
									<th scope="col">Tipo Atto</th>
									<th scope="col">Struttura amministrativa</th>
									<th scope="col">Istituto di credito</th>
									<th scope="col">Importo</th>
									<th class="tab_Right" scope="col"></th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
						
						<s:include value="/jsp/mutuo/include/modaleVariazioneTassoMutuiSelezionati.jsp" />
						
						<s:hidden id="HIDDEN_startPosition" name="startPosition" value="%{savedDisplayStart}" />
						
						<div id="msgAnnulla" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
							<s:hidden id="HIDDEN_UidDaAnnullare" name="mutuo.uid" />
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
								<button class="btn btn-primary" formmethod="post" type="submit" formaction="risultatiRicercaMutuo_annulla.do">si, prosegui</button>
							</div>
						</div>

						
						<p>
							<s:include value="/jsp/include/indietro.jsp" />
                           <s:if test="abilitaVariazioneTasso">
                            <span id="abilitaVariazioneTasso" class="hide"></span>
							<button disabled="disabled" type="button" id="variazioneTassoMutuiSelezionati" class="btn selezionati">variazione tasso mutui selezionati</button>
                           </s:if>
						</p>  	 
					</form>
				</div>	
			</div>	
		</div>	 
	</div>	

	<s:include value="/jsp/include/footer.jsp" />

	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/mutuo/risultatiRicercaMutuo.js"></script>
</body>
</html>