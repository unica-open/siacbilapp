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
						<h3>Risultato ricerca causale incasso</h3>	
						<h4><s:property value="riepilogoRicerca"/></h4>				
						<h4><span id="id_num_result" class="num_result"></span></h4>
					
							<table class="table table-hover tab_left dataTable" id="risultatiRicercaCausale" summary="...." >
							<thead>
								<tr>
									<th scope="col">Causale</th>
									<th scope="col">Tipo causale</th>
									<th scope="col">Stato causale</th>
									<th scope="col">Struttura amministrativa</th>
									<th scope="col">Capitolo </th>
									<th scope="col">Accertamento</th>	
									<th scope="col">Soggetto</th>			  
									<th scope="col">Provvedimento</th>
									<th scope="col">&nbsp;</th>
									<th scope="col"><i class="icon-spin icon-refresh spinner" id="SPINNER_ConsultaCausale"></i></th> <!-- spinner -->             
								</tr>
							</thead>
							<tbody>	
							</tbody>
							<tfoot>
							</tfoot>
						</table>
						
						<s:hidden id="HIDDEN_startPosition" name="startPosition" value="%{savedDisplayStart}" />

						<div id="msgAnnulla" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
							<s:hidden id="HIDDEN_UidDaAnnullare" name="uidDaAnnullare" />
							<div class="modal-header">
								<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
								<h4 class="nostep-pane">Annullamento </h4>
							</div>
							<div class="modal-body">
								<fieldset class="form-horizontal">
									<div class="control-group">
										<label for="dataScadenza" class="control-label">Data decorrenza&nbsp;</label>
										<div class="controls">
											<s:textfield id="dataScadenza" cssClass="span2 datepicker" name="causale.dataScadenza" placeholder="dataDecorrenza" />
										</div>
									</div>
								</fieldset>
							</div> 
							<div class="modal-footer">
								<button class="btn btn-secondary" data-dismiss="modal" aria-hidden="true">annulla</button>
								<button class="btn btn-primary" formmethod="post" type="submit" formaction="risultatiRicercaCausaleEntrataAnnulla.do">conferma</button>
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
	<s:include value="/jsp/causale/consultaCausaleEntrata_modale.jsp" />
	<s:include value="/jsp/include/footer.jsp" />	
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/causale/risultatiRicercaEntrata.js"></script>
</body>
</html>