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
				    <h3><span id="id_num_result" class="num_result"></span> Risultati trovati</h3>                           
					<!-- TABELLE RIEPILOGO con azioni -->
					<table class="table table-striped table-bordered table-hover dataTable" id="risultatiRicercaVincolo" summary="...." >
						<thead>
							<tr>
								<th scope="col">Codice vincolo</th>
								<th scope="col">Bilancio</th>
								<th scope="col">Trasferimenti vincolati</th>
								<th scope="col">n&deg; capitoli entrata</th>
						        <th scope="col">n&deg; capitoli spesa</th>
								<th scope="col">Azioni</th>
							</tr>
						</thead>	
						<tbody>
						</tbody>	
					</table>
					<s:hidden id="HIDDEN_startPosition" name="startPosition" value="%{savedDisplayStart}" />
					
					<!-- Modale ANNULLA -->
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
								<button class="btn btn-primary" formmethod="post" type="submit" formaction="risultatiRicercaVincoloAnnulla.do">si, prosegui</button>
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
	<script type="text/javascript" src="${jspath}vincolo/risultatiRicerca.js"></script>
</body>
</html>